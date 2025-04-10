import matplotlib.scale as mscale
import matplotlib.pyplot as plt
import matplotlib.transforms as mtransforms
import matplotlib.ticker as ticker
import numpy as np

class SquareRootScale(mscale.ScaleBase):
    """
    ScaleBase class for generating square root scale.
    Reference: https://matplotlib.org/stable/devel/add_new_projection.html
    """
 
    name = 'sqrt'
 
    def __init__(self, axis, **kwargs):
        # note in older versions of matplotlib (<3.1), this worked fine.
        # mscale.ScaleBase.__init__(self)

        # In newer versions (>=3.1), you also need to pass in `axis` as an arg
        mscale.ScaleBase.__init__(self, axis)
 
    def set_default_locators_and_formatters(self, axis):
        axis.set_major_locator(ticker.AutoLocator())
        axis.set_major_formatter(ticker.ScalarFormatter())
        axis.set_minor_locator(ticker.AutoMinorLocator()) #NullLocator())
        axis.set_minor_formatter(ticker.NullFormatter())
        # todo: implement minor locator along the lines of
        #     def getMinors(ticks):
        #        widths = ticks[1:] - ticks[:-1]
        #        minorTicks = []
        #        steps = [5, 4, 2, 1]
        #        last = None
        #        for i, (t, w) in enumerate(zip(ticks[:-1], widths)):
        #            if i == len(widths):
        #                sw = last
        #                break
        #            if i == len(widths) - 3:
        #                steps = steps[1:]   
        #            for step in steps:
        #                p = int(np.floor(np.log10(w))) - 1
        #                f = 1
        #                if p < 0:
        #                    f = np.power(10, -p + 1)
        #                w *= f
        #                sw = w // step
        #                if sw == 0:
        #                    continue
        #                if sw == w / step:
        #                    sw /= f
        #                    if last is None:
        #                        last = sw
        #                    break
        #            for j in range(step):
        #                minorTicks.append(t + j*sw)
        #        return np.unique(minorTicks)        
        
    def limit_range_for_scale(self, vmin, vmax, minpos):
        return vmin, vmax
 
    class SquareRootTransform(mtransforms.Transform):
        input_dims = 1
        output_dims = 1
        is_separable = True
 
        def transform_non_affine(self, a): 
            return np.sign(a) * np.sqrt(np.abs(a))
 
        def inverted(self):
            return SquareRootScale.InvertedSquareRootTransform()
 
    class InvertedSquareRootTransform(mtransforms.Transform):
        input_dims = 1
        output_dims = 1
        is_separable = True
 
        def transform(self, a):
            return np.array(a)**2
 
        def inverted(self):
            return SquareRootScale.SquareRootTransform()
 
    def get_transform(self):
        return self.SquareRootTransform()
 
mscale.register_scale(SquareRootScale)

