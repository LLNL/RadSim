import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl

class Layer(object):
    def __init__(self, thickness, density=0, sigma=0, forward=0):
        self.thickness = thickness
        self.density = density
        self.sigma = sigma
        self.forward = forward

    def __repr__(self):
        return "Layer %d"%self.index

class Model(object):
    def __init__(self, layers):
        self.layers = layers
        r = 0
        for i,l in enumerate(layers):
            l.index = i
            l.inner = r
            r += l.thickness
            l.outer = r
            l.volume = 4/3*np.pi*(l.outer**3-l.inner**3)
            l.mass = l.volume*l.density
            print("layer", l.inner, l.outer)

    def index(self, radius):
        for i, l in enumerate(self.layers):
            if l.inner<=radius and radius<l.outer:
                return i
        return -1

    def trace(self, r0, theta):
        # First figure out which layer we are in
        i = self.index(r0)
        r = r0
        d = []
        # We started outside the object, no matter check to see if we hit the outer layer
        if i==-1:
            layer = self.layers[-1]
            u = -r*np.cos(theta)
            q = layer.outer**2-r**2*np.sin(theta)**2
            if u>0 and q>0:
                d0 = u - np.sqrt(q)
                d.append((None,d0,theta))
                theta = np.arcsin(r/layer.outer*np.sin(theta))
                r = -layer.outer
                i = len(self.layers)-1
            else:
                return d

        # We are inside the model, work our way out
        for j in range(len(self.layers)*2):
            if i==len(self.layers):
                break
            layer = model.layers[i]
            u = -r*np.cos(theta)
            q = layer.inner**2-r**2*np.sin(theta)**2
            # Going in
            if u>0 and q>0: # and r!=layer.inner:
                d0 = u - np.sqrt(q)
                #print("inward", r, -r*np.cos(theta), -r*np.cos(theta) + np.sqrt(q), -r*np.cos(theta)-np.sqrt(q))
                d.append((layer,d0,theta))
                theta = np.arcsin(r/layer.inner*np.sin(theta))
                r = -layer.inner
                i = i -1
                continue
            # Going out
            q = layer.outer**2-r**2*np.sin(theta)**2
            if q<=0:
                break
            #print("outward",r, theta)
            d0 = u + np.sqrt(q)
            d.append((layer, d0, theta))
            theta = np.arcsin(r/layer.outer*np.sin(theta))
            r = layer.outer
            i = i + 1
        return d

def escape(model, layer, n=30, m=30, kd=6):
    sa = 3.7e10 / layer.volume

    # Compute a mesh based on the skin depth of the material
    depth = 1/(layer.sigma*layer.density)
    thickness = layer.thickness
    if thickness>kd*depth:
        rmesh = np.linspace(layer.outer-kd*depth, layer.outer, n+1)
    else:
        rmesh = np.linspace(layer.inner, layer.outer, n+1)

    # Fixed angle mesh
    tmesh = np.linspace(0, np.pi, m+1)
    S = 0
    R = np.zeros((n,m))

    # Best to work the inner as radial for integration
    for j in range(m):
        t0 = tmesh[j]
        t1 = tmesh[j+1]
        tm = (t0+t1)/2

        r0 = rmesh[0]
        tr0 = model.trace(r0,tm)
        q0 = np.sum([-i[0].sigma*i[0].density*i[1] for i in tr0 if i[0] is not None])

        for i in range(n):
            r0 = rmesh[i]
            r1 = rmesh[i+1]
            rm = (r0+r1)/2

            # Compute the path to surface
            tr1 = model.trace(r1,tm)

            # Convert to escape probability
            q1 = np.sum([-i[0].sigma*i[0].density*i[1] for i in tr1 if i[0] is not None])
            u = (q1-q0)/(r1-r0)

            # Total
            v = (np.exp(q1) - np.exp(q0))/u * np.sin(tm) * (t1-t0) * rm**2
            R[i,j] = v
            S += v

            q0 = q1
            r0 = r1

    if False:
        plt.imshow(R)
        plt.ylabel('radius')
        plt.xlabel('theta')
    return 2*np.pi*S*sa


def escape2(model, emitting, n=80, plot=True):
    sa = 3.7e10 / emitting.volume
    cmap = mpl.colormaps['hsv']
    R = model.layers[-1].outer

    # Compute the range of angles that intersect the emitting surface
    phimesh = np.linspace(0, np.arcsin(emitting.outer/R), n+1)
    dphi = phimesh[1]-phimesh[0]

    S = 0
    for j in range(n):
        phi0 = phimesh[j]
        phi1 = phimesh[j+1]
        phi = (phi0+phi1)/2
        trace = model.trace(-R,phi)

        # Start with choord importance
        P = np.sin(phi)*np.cos(phi)

        # Distance from surface
        #l = 0
        # Traverse the choord intersecting the object
        for layer,v,angle in trace:

            # Void layers contribute nothing
            if layer is None:
        #        l += v
                continue

            # Compute the attenuation constant
            k = layer.sigma*layer.density

            # If this is the emitting layer, then we will add the contribution
            if layer == emitting:
                S += P*(1-np.exp(-k*v))/k

            # Add the attenuation for the next segment
            P *= np.exp(-k*v)
        #    l += v

        # Plotting
        if plot:
            x = -R
            y = 0
            cP = np.cos(phi)
            sP = np.sin(phi)
            for l,v,t in trace:
                x1 = v*cP + x
                y1 = v*sP + y
                if l is not None:
                    p = l.index
                    plt.plot([x,x1], [y, y1], c=cmap(20*p))
                x = x1
                y = y1

    return sa*S*2*np.pi*R**2*dphi

def solid(emitting):
    """ Analytic integration of choords """
    sa = 3.7e10 / emitting.volume
    k = emitting.sigma*emitting.density
    R = emitting.outer
    return sa * np.pi/2/k**3 *(-1 + 2*k**2*R**2 + np.exp(-2*k*R) * (1+2*k*R))

def parts2(emitting, n=40):
    """ Numerical integration of choords """
    sa = 3.7e10 / emitting.volume
    k = emitting.sigma*emitting.density
    R = emitting.outer
    pmesh = np.linspace(0, np.pi/2, n+1)
    pmesh = (pmesh[1:]+pmesh[:-1])/2
    dp = pmesh[1]-pmesh[0]
    S = 0
    for phi in pmesh:
        g = np.sin(phi)*np.cos(phi) * (1/k-np.exp(-k*2*R*np.cos(phi)))
        S += dp*g
    return sa*S*2*np.pi*R**2

# Integral exp(-k (x-r))  = -1/k exp(-k(x-r))
# Integral x exp(-k (x-r))  = -(x/k+1/k^2) exp(-k(x-r))
# Integral x^2 exp(-k (x-r)) = -(x^2/k + 2x/k^2 + 2/k^3) exp(-k(x-r)) 



#model = Model([ Layer(4), Layer(1), Layer(2), Layer(0.1), Layer(0.9)])
#model = Model([Layer(4, 11.35, 6.803E-02)])  # 1 MeV
model = Model([Layer(4, 11.35, 5.336e-00)])  # 100 keV
#model = Model([Layer(4, 11.35, 3.733E-01)])  # 300 keV
#model = Model([Layer(4, 11.35, 4.530E-02)])  # 2 MeV

#model = Model([Layer(0.5, 11.35, 4.530E-02), Layer(2.5, 0, 0), Layer(1, 11.35, 4.530E-02)])  # 2 MeV

print(escape(model, model.layers[0]))
print(escape2(model, model.layers[0]))
print(solid(model.layers[0]))
print(parts2(model.layers[0]))
#print(escape2(model, model.layers[0], n=200))

if False:
    print(model.trace(4.5, 0))
    print(model.trace(4.5, np.pi))

if False:
    cmap = mpl.colormaps['hsv']
    x0 = 7.1
    theta = np.linspace(0,1,1001)*np.pi

    for x0 in np.linspace(0, 10, 20):
        for th in np.linspace(0, 1, 21)*np.pi:
            d = model.trace(x0, th)
            x = x0
            y = 0
            for l,v,t in d:
                x1 = v*np.cos(th) + x
                y1 = v*np.sin(th) + y
                if l is not None:
                    p = l.index
                    plt.plot([x,x1], [y, y1], c=cmap(20*p))
                x = x1
                y = y1

    for l in model.layers:
        plt.plot(l.outer*np.cos(theta), l.outer*np.sin(theta), c='k')

plt.show()
            
