import numpy as np
import jpype
import jpype.imports
jpype.startJVM(classpath=["../../dist/*", "../../../gov.llnl.utility/dist/*"])

import java
print(java.lang.System.getProperty('java.class.path'))
from gov.llnl.math.algebra import CholeskyFactorization
from gov.llnl.math.matrix import Matrix
from gov.llnl.math.matrix import MatrixOps
from gov.llnl.math.matrix import MatrixFactory
from gov.llnl.math.matrix.special import MatrixSymmetric
import pickle

def genProblem(size, ratio):
    while True:
        # Create problems with specified size and domain.
        eig = 10**np.random.uniform(-np.log(ratio)/np.log(10),0, (size,))
        eig[0]=1
        eig.sort()
        
        A=np.eye(size)
        for i in range(size):
            # we need to create an matrix with uniform eigvalues that we can then scale to 
            # arbitrary lengths.  We can do this by applying householder rotations to 
            # eye matrix and then use the columns of the resulting matrix as which will
            # produce sample problems with the required characteristics.

            # generate a random vector for rotation
            C= np.random.uniform(-1,1,(size,1))

            # preform a householder rotation
            C= C/np.sqrt(np.sum(C**2))
            T=np.eye(size)-2*C@C.T

            # update the matrix and repeat
            A=T@A

        # Preform the rotation and scaling
        B = np.zeros((size,size))
        for i in range(size):
           B = B+np.outer(A[:,i],A[:,i])*eig[i]

        # Validate
        invalid = False
        for i in range(size):
            dif = B@A[:,i]-A[:,i]*eig[i]
            if (dif@dif>10**-29):
                invalid = True

        if not invalid:
            return B

    # This algorithm was checked using the eig function in np.linalg.  The 
    # eigvalues selected appear correct up to 10**8.  After that the 
    # process of extracting eig values 

size = 20
scale = 10.0**18
r1 = []
r2 = []
for i in range(10000):
    try:
        A = genProblem(size, scale)
        Y = np.random.normal(-1,1,(size,))
        B = A@Y+0.001*np.random.normal((size,)) # Add a noise vector so there is no exact answer

        # Numpy solve method
        eY = np.linalg.solve(A,B)
        e = (B-A@eY)
        mse1 = e@e/size
        r1.append(mse1) # mse

        # Livermore hardened algorithm
        cf = CholeskyFactorization()
        m = MatrixFactory.wrapArray(A.flatten(),size,size)
        ms = MatrixSymmetric(m)
        cf.decompose(ms)
        eY2 = cf.solve(B)
        e = (B-A@eY2)
        mse2 = e@e/size
        r2.append(mse2) #mse
        if (mse1>10.0**4 and mse2<10.0**-2):
            print("Found")
            out={}
            out['A']=A
            out['B']=B
            out['Y1']=np.array(eY)
            out['Y2']=np.array(eY2)
            out['mse1']=mse1
            out['mse1']=mse1
            pickle.dump(out, open('problem%d.pic'%i,'wb'))
            
    except Exception as ex:
        print(ex)
        pass


import matplotlib.pyplot as plt
plt.plot(r1, label='numpy.linalg.solve')
plt.plot(r2, label='gov.llnl.math.algebra.CholeskyFactorization')
plt.yscale('log')
plt.title('Solution error size=%d scale=%e'%(size, scale))
plt.legend()
plt.ylabel("MSE")
plt.xlabel("Trial")
plt.show()

print("numpy.linalg.solve mean Error:", np.mean(r1))
print("numpy.linalg.solve std Error:", np.std(r1))
print("gov.llnl.math.algebra.CholeskyFactorization mean Error:", np.mean(r2))
print("gov.llnl.math.algebra.CholeskyFactorization std Error:", np.std(r2))


