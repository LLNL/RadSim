import matplotlib.pyplot as plt
import numpy as np

y = np.amax(np.random.normal(size=(50,10000)), axis=0)
y2 = np.amax(np.random.normal(loc=2, scale=4, size=(50,10)), axis=0)
y=np.hstack((y,y2))

class Gumbel:
    def __init__(self, mu, beta):
        self.beta = beta
        self.mu = mu

    def icdf(self, pct):
        return -self.beta * np.log(-np.log(pct)) + self.mu

def fitqq(y, p1, p2):
    beta = 0.
    mu = 0.
    y = np.sort(np.array(y))
    lenin = len(y)
    i1 = int(np.floor(lenin * p1))
    i2 = int(np.floor(lenin * p2))
    y1 = -np.log(-np.log(np.array(range(i1+1,i2+1))/(lenin+2)))
    A1 = np.dot(y[i1:i2], y[i1:i2])
    A2 = -np.sum(y[i1:i2])
    A3 = i2-i1
    B1 = np.dot(y1, y[i1:i2])
    B2 = -np.sum(y1)
    A = np.array( ((A1,A2),(A2,A3)) )
    B = np.array( (B1,B2) )
    k = np.linalg.solve(A,B)
    beta = 1.0/k[0]
    mu = k[1]*beta
    return Gumbel(mu, beta)

# distibution is the inverse cumulative function which takes numbers between 0 and 1 
def plotqq(measurements, icdf):
    y = np.sort(np.array(measurements))
    n = len(y)
    # we are going to add two observations so we don't hit 0 and 1 on the cff
    u = np.linspace(1/n, 1-1/n, n)
    x = icdf(u)
    plt.plot(x, y, '.')
    mn = np.min(y)
    mx = np.max(y)
    plt.plot((mn,mx),(mn,mx))
    plt.xlabel('Theoretical Quantiles')
    plt.ylabel('Observed Quantiles')

gd = Gumbel(3, 2)
print(gd.icdf(0.001))
print(gd.icdf(1-0.001))

fit = fitqq(y, 0.8, 0.98)
print(" 1/12 hour setting is ",fit.icdf(1-1/12/len(y)))
plotqq(y, lambda p: fitqq(y, 0.8, 0.98).icdf(p))
for i in range(1,11):
    plt.axhline(fit.icdf(1-1/i/len(y)))
plt.show()


