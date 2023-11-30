////////////////////////////////////////////////////////////////////////////////
/// FUNCTION: AreaMatchedHermitSpline()
/// PURPOSE:  Smooth, exact fitting of bin data
/// METHOD:   Hermite Spline with area matching
/// AUTHOR:   JIN YAO  
///////////////////////////////////////////////////////////////////////////////
#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>

#define SGN(x) ((x >= 0.0) ? 1 : -1)
#define MIN(x, y) ((x < y) ? x : y)
#define MAX(x, y) ((x >= y) ? x : y)

#define BLANK -1
#define NEGATIVE 0

#define MEGA  1.0e+6
#define MICRO 1.0e-6
#define PICO  1.0e-12
#define HALF  0.5

#define MAX_NUM_ITER 36

// base functions for a Hermit Cubic Spline
#define H00(t) ((t) * (t) * (t + t - 3) + 1.0)
#define H10(t) ((t) * (t - 1) * (t - 1))
#define H01(t) ((t) * (t) *(3 - t - t))
#define H11(t) ((t) *(t) *(t - 1))

// area integrals of Hermite base functions
#define I00 HALF
#define I10 (1.0 / 12.0)
#define I01 HALF
#define I11 (-I10)

// area integral between (0, u)
#define K00(t) (t*(1.0 - t*t*(1.0 - 0.5*t)))
#define K10(t) (t*t*(6.0 - t*(8.0 - 3*t)) / 12.0)
#define K01(t) ((1.0 - 0.5*t)*t*t*t)
#define K11(t) (t*t*t*(3*t - 4.0) / 12.0)

// input data points
int numDataPoints;

// number of intervals (bins) associated with input data points
int numIntervals;

// the desired number of bins with even counts 
int numDesiredBins;

// estimate derivatives at knots
void EstimateDerivatives(double *dx, double *h, double *s, 
			 double *sp, double *spp, int numIntervals);

// re-estimate slopes in slope space to be consistent to the first Hermite spline
void UpdateSlopeSpace(double *x, double *y, double *dx, double *h, 
		       double *X, double *Y, double *spp, int numIntervals);

// match area in each bin exactly with Hermite spline on the new control points
void HermitSplineAreaMatch(double *xnew, double *ynew, double *dy, double *x, double *dx, 
			   double *h, double *ds, double *y, double *X, double *Y, 
			   int numDataPoints, int numIntervals);

// detect possible negativeness in isolated bins
void DetectNegativeness(double *xnew, double *ynew, double *dy, double *x, double *dx, 
			double *h, double *y, double *X, double *Y, int *binTag, 
			int numDataPoints, int numIntevals, int numSubIntervals);

// update sum of areas with new control points
void AreaSumUpdate(double *xnew, double *ynew, double *snew, double *dy, 
		   double *x, double *dx, double *h, int *binTag, 
		   int numDataPoints, int numIntervals, int numSubIntervals);

// back solve for even bin count in each new bin
void BackSolveNewBins(double *xnew, double *ynew, double *snew, double *dy, double *s, 
		      double *x, double *dx, double *h, double *xstar, int *binTag,
		      int numDataPoints, int numIntervals, int numSubIntervals);

// finding new knot with matching area in the solution bin
double KnotLocation(double *xnew, double *ynew, double *snew, double *dy, 
		    double s0, int i);

// fill in values for an int array
void FillIntArray(int *array, int N, int value);

// fill in values for a double array
void FillDoubleArray(double *array, int N, double value);

// treat isolated interior negativeness
void TreatNegativeness(int i, double *xnew, double *ynew, double *dy, double *h);

// plotting the input
void PlotInput(double *x, double *h, double *s, int N);

// plot the area matched hermite spline
void PlotSolution(double *xnew, double *ynew, double *dy,  
		  int numControlPoints, int numSubIntervals);

// plot the newly binned data
void PlotNewBinData(double *xstar, double ds0, int numNewBins);

// solver of a tri-diagonal linear system
void TriDiagonalSolver(double *x, const double *a, const double *b, const double *c, 
		       const int N) ;

int main()
{
  int pass = BLANK;
    
  //////////////////////////////////////////////////////////////////////////
  ////////////////////  READ IN THE INPUT DATA FILE   //////////////////////
  //////////////////////////////////////////////////////////////////////////

  FILE *dataPtr;
  dataPtr = fopen("testNegatives.dat", "r");

  fscanf(dataPtr, "%d %d \n", &numDataPoints, &numDesiredBins);

  // set number of intervals
  numIntervals = numDataPoints - 1; // for an open curve

  // the x-coordinates of the knots
  double *x = (double *) malloc(numDataPoints*sizeof(double));

  // the bin widths
  double *dx = (double *) malloc(numIntervals*sizeof(double));

  // the average "height" (count per energy unit) in each bin
  double *h = (double *) malloc(numIntervals*sizeof(double));

  // the input data (bin counts)
  double *ds = (double *) malloc(numDataPoints*sizeof(double));

  // the accumulated counts in each bin
  double *s = (double *) malloc(numDataPoints*sizeof(double));

  for(int i = 0; i < numDataPoints; i++) {
    int svar;
    int xvar;

    fscanf(dataPtr, "%d %d \n", &xvar, &svar);
    x[i] = (double) xvar;
   
    if(i == 0) {
      s[i] = (double) svar;
    }
    else {
      s[i] = s[i - 1] + (double) svar;
    }
  }

  ////////////////////////////////////////////////////////////////////
  //////////  SET THE ESSENTIAL ARRAYS FOR COMPUTATION ///////////////
  ////////////////////////////////////////////////////////////////////

  // counts in each bin 
  for(int i = 0; i < numIntervals; i++) {
    ds[i] = s[i + 1] - s[i];
  }

  // then the bin width and the average count per unit
  for(int i = 0; i < numIntervals; i++) {
    dx[i] = x[i + 1] - x[i];
    h[i] = ds[i] / dx[i];
  }

  // plot the input data
  PlotInput(x, h, s, numDataPoints);

  ////////////////////////////////////////////////////////////////////
  //////////////////   PROBLEM INITIALIZATION    /////////////////////
  ////////////////////////////////////////////////////////////////////

  // slope of "g" (g') of the monotone interpolation at knots
  double *sp = (double *) malloc(numDataPoints*sizeof(double));
  // slope of "f" (f = g', so spp is f' = g") at knots
  double *spp = (double *) malloc(numDataPoints*sizeof(double));

  // now compute the first and second derivatives from the data
  EstimateDerivatives(dx, h, s, sp, spp, numIntervals);

  ///////////////////////////////////////////////////////////////////////
  ///////////// MAIN OPERATION: THE HERMIT SPLINE THAT //////////////////
  ///////////// MATCHES AREAS EXACTLY WITH NO OSCILLATION ///////////////
  ///////////////////////////////////////////////////////////////////////

  // the solution array
  double *y = (double *) malloc(numDataPoints*sizeof(double));

  // create a set of data points
  double *X = (double *) malloc(numDataPoints*sizeof(double));
  double *Y = (double *) malloc(numDataPoints*sizeof(double));
  
  // re-estimate slopes to be consistent to the first Hermite spline that
  // intersects the bin walls for control points with integer subscripts
  UpdateSlopeSpace(x, y, dx, h, X, Y, spp, numIntervals);

  ////////////////////////////////////////////////////////////////////
  ////////// I MATCH LOCAL AREAS EXACTLY WITH ALL CONTROL ////////////
  ////////// POINTS FOR THE FINAL HERMITE SPLINE SOLUTION ////////////
  ////////////////////////////////////////////////////////////////////

  // new number of control points
  int numControlPoints = 2*numIntervals + 1;
  int numSubIntervals = numControlPoints - 1;

  // new knots for cubic spline
  double *xnew = (double *) malloc(numControlPoints*sizeof(double));
  double *ynew = (double *) malloc(numControlPoints*sizeof(double));

  // slope at the new knots
  double *dy = (double *) malloc(numControlPoints*sizeof(double));

  HermitSplineAreaMatch(xnew, ynew, dy, x, dx, h, ds, y, X, Y, 
  			numDataPoints, numIntervals);
  
  ///////////////////////////////////////////////////////////////
  ////////// DETECT AND FIX POSSIBLE NEGATIVENESS ///////////////
  ///////////////////////////////////////////////////////////////
  
  // to tag the bins with possible negativeness
  int binTag[numIntervals];
  FillIntArray(binTag, numIntervals, 1);
  
  // compute minimum in each sub bin and if negativeness is observed
  // (assumed isolated), do a local fix and make the minimums zeros.
  DetectNegativeness(xnew, ynew, dy, x, dx, h, y, X, Y, binTag, 
		     numDataPoints, numIntervals, numSubIntervals);

  // treat negativeness;
  for(int i = 0; i < numIntervals; i++) {
    if(binTag[i] == NEGATIVE) {
      TreatNegativeness(2*i, xnew, ynew, dy, h);  
    }
  }

  // plot the hermit spline curve
  PlotSolution(xnew, ynew, dy, numControlPoints, numSubIntervals);

  // sum of counts at each new control point
  double *snew = (double *) malloc(numControlPoints*sizeof(double));
  
  AreaSumUpdate(xnew, ynew, snew, dy, x, dx, h, binTag,
		numDataPoints, numIntervals, numSubIntervals);


  /////////////////////////////////////////////////////////////////
  ///////////  BACK SOLVE FOR EVEN COUNTS IN EACH BIN  ////////////
  /////////////////////////////////////////////////////////////////

  // xstar the new bin knots
  double *xstar = (double *) malloc((numDesiredBins + 1)*sizeof(double));

  // the desired count each bin
  double ds0 = s[numIntervals] / ((double) numDesiredBins);
  
  BackSolveNewBins(xnew, ynew, snew, dy, s, x, dx, h, xstar, binTag,
		   numDataPoints, numIntervals, numSubIntervals);

  // plot the new bin data
  PlotNewBinData(xstar, ds0, numDesiredBins);

  free(x);
  free(dx);
  free(s);
  free(ds);
  free(h);
  free(y);
  free(X);
  free(Y);
  free(xnew);
  free(ynew);
  free(snew);
  free(xstar);

  pass++; // passed
  return pass;
}



// estimate derivatives at knots
void EstimateDerivatives(double *dx, double *h, double *s, 
			 double *sp, double *spp, int numIntervals)
{
  // i fit a quadratic to compute "sp" and "spp" for internal nodes.
  // s(x) = alpa (x - x[i])^2 + beta (x - x[i]) + gama, only the
  // interior knots are treated, boundary condition to be added later

  for(int i = 1; i < numIntervals; i++) {
    
    double var = (dx[i] + dx[i - 1]);  
    double alfa = (h[i] - h[i - 1]) / var;
    double beta = (h[i]*dx[i - 1] + h[i - 1]*dx[i]) / var;
    double gama = s[i];

    sp[i] = beta;
    spp[i] = alfa + alfa;
  }
}



// re-estimate slopes in slope space to be consistent to the first Hermite spline
void UpdateSlopeSpace(double *x, double *y, double *dx, double *h, 
		      double *X, double *Y, double *spp, int numIntervals)
{
 // x-coordinates
  double xLL, xL, xR, xRR;
  
  // ghost y-coordinate preset for applying boundary conditions
  double yLL, yL, yR, yRR;
  
  // differential at the two ends
  double sL, sR;
  
  // left boundary conditions
  double delta;
  double dh;
  
  // left boundary ghost point (counter symmetric condition)
  delta = HALF*dx[0];
  dh = h[1] - h[0];
  xLL = -delta;
  yLL = h[0] - dh;
  
  // right boundary ghost point (symmetric condition);
  delta = 0.5*dx[numIntervals - 1];
  dh = h[numIntervals - 1] - h[numIntervals - 2];
  xRR = x[numIntervals - 1] + delta;
  yRR = h[numIntervals - 1];
  
  // the left end
  X[0] = HALF*dx[0];
  Y[0] = h[0];
  
  // the right end
  X[numDataPoints - 1] = 
    HALF*(x[numDataPoints - 2] + x[numDataPoints - 1]);
  Y[numDataPoints - 1] = h[numIntervals -1];
  
  int iter = 0;
  
  //////////////////////////////////////////////////////////////////
  /////////////////  FIX THE SLOPE AT KNOTS   //////////////////////
  //////////////////////////////////////////////////////////////////
  while(iter < 16) {
    // quadratic area matched fitting for the interior points
    // the iteration here is in order to make slope consistent
    // with the current Hermit spline (on mid bin control points)
    for(int i = 1; i < numIntervals; i++) {
      
      double lift = 0.0;
      
      sL = spp[i];
      sR = spp[i + 1];
      
      // apply lift(by fitting a quadratic)
      lift = dx[i]*(sL - sR)/ 24.0;
      
      X[i] = x[i] + HALF*dx[i];
      Y[i] = h[i] + lift;
    }
    
    // update the slope "spp" 
    for(int i = 0; i < numIntervals; i++) {
      
      double xL = X[i];
      double yL = Y[i];
      
      double xR = X[i + 1];
      double yR = Y[i + 1];
      
      xLL = x[0];
      yLL = h[0] - HALF*(h[1] - h[0]);
      
      if(i > 0) {
	xLL = X[i - 1];
	yLL = Y[i - 1];
      }
      
      xRR = x[numIntervals - 1];
      dh =  h[numIntervals - 2] - h[numIntervals - 1];
      yRR = h[numIntervals - 1] - HALF*dh;
      
      if(i < numIntervals - 1) {
	xRR = X[i + 2];
	yRR = Y[i + 2];
      }
      
      double syL = HALF*(yR - yLL);
      double syR = HALF*(yRR - yL);

      double xOld = xL;
      double yOld = yL;
      
      for(int j = 0; j < 20; j++) {
	
	double u = 0.05*j;
	double xC = xL + u*(xR - xL);
	double yC = yL*H00(u) + syL*H10(u) + yR*H01(u) + syR*H11(u);
	
	// estimate intersection of curve "i" and the cell wall xW = x[i], 
	// for finding a good initial guess of height of curve on a wall.
	
	double xW = x[i + 1];
	if(xOld <= xW && xC > xW) {
	  // the intersection is found
	  double ratio = (xW - xOld) / (xC - xOld);
	  y[i + 1] = (1.0 - ratio)*yOld + ratio*yC;
	  
	  // update slope
	  spp[i + 1] = (yC - yOld) / (xC - xOld);
	}
	
	xOld = xC;
	yOld = yC;
	
      }
    }

    iter++;
  }
}



// match area in each bin exactly with Hermite spline on the new control points
void HermitSplineAreaMatch(double *xnew, double *ynew, double *dy, double *x, double *dx, 
			   double *h, double *ds, double *y, double *X, double *Y, 
			   int numDataPoints, int numIntervals)
{
  // what it really does is to define (xnew, ynew) as the new bin system with
  // number of bins doubled

  // I use a tridiagonal liner solver to do the area matching.
  // note the system has a strong diagonal line, a good feature

  // define the diagonals
  double *c = (double *) malloc((numIntervals - 1)*sizeof(double));
  double *a = (double *) malloc((numIntervals - 1)*sizeof(double));
  double *b = (double *) malloc(numIntervals*sizeof(double));

  FillDoubleArray(c, numIntervals - 1, -1.0);
  FillDoubleArray(a, numIntervals - 1, -1.0);
  FillDoubleArray(b, numIntervals , 26.0);

  // *the solution vector
  double *z = (double *) malloc(numIntervals*sizeof(double));

  // fill "z" with the right hand side initially

  // ghost knot over left boundary (counter symmetric)
  double gyL = 2*h[0] - h[1]; 
  
  // ghost knot over the right boundary (symmetric)
  double gyR = h[numIntervals - 1];

  // left boundary
  y[0] = h[0] - HALF*(h[1] - h[0]);

  // right boundary
  y[numIntervals] = h[numIntervals - 1] -
    HALF*(h[numIntervals - 2] - h[numIntervals - 1]);

  double RHS[numIntervals];

  for(int i = 0; i < numIntervals; i++) {
    
    double ys = y[i] + y[i + 1];
    double delta = 0.0;

    if(i == 0) {
      delta = gyL;
    }		
    
    if(i == numIntervals - 1) {
      delta = gyR;
    }

    RHS[i] = 48*h[i] - 12*ys + delta;
  }

  for(int i = 0; i < numIntervals; i++) {
    z[i] = RHS[i];
  }

  ////////////////////////////////////////////////////////////////////
  //////////////////  SOLVE THE TRI-DIAGONAL SYSTEM  /////////////////
  ////////////////////////////////////////////////////////////////////

  TriDiagonalSolver(z, a, b, c, numIntervals);
 
  // check area match
  for(int i = 0; i < numIntervals; i++) {
    
    double term1 = 0.5*(y[i] + y[i + 1] + 2*z[i]);
    double term2;

    if(i == 0) {
      term2 = (2*z[i] - gyL - z[i + 1]) / 24.0;
    }
    else if(i == numIntervals - 1) {
      term2 = (2*z[i] - z[i - 1] - gyR) / 24.0;
    }
    if(i > 0 && i < numIntervals - 1) {
      term2 = (2*z[i] - z[i - 1] - z[i + 1]) / 24.0;
    }

    double area = 0.5*dx[i]*(term1 + term2);
  }

  // define (xnew, ynew) next
  // xnew is fixed
  int count = 0;
  for(int i = 0; i < numIntervals; i++) {
    double xknot = x[i];
    xnew[count] = xknot;
    count++;
    xnew[count] = xknot + HALF*dx[i];
    count++;
  }

  // x-coordinate of the control point on the right boundary
  xnew[count] = x[numDataPoints - 1];

  // ynew is fixed on bin walls

  // the count of control points
  count = 0;

  for(int i = 0; i < numIntervals; i++) {
    // keep value at original knots above zero 
    ynew[count] = MAX(0.0, y[i]); // these are above the knots, fixed
    count++;
    // ynew[count] = Y[i]; // these are above mid-bin point, to vary for area match
    ynew[count] = z[i];
    count++;
  }

  // y-component of the control points on the right boundary (symmetric)
  ynew[count] = y[numIntervals];
    
  // evaluate slope at knots
  for(int i = 0; i < 2*numIntervals + 1; i++) {
    
    if(i == 0) {
      dy[i] = 0.5*(ynew[i + 1] - gyL);
    }
    else if(i == 2*numIntervals) {
      dy[i] = 0.5*(gyR - ynew[i - 1]);
    }
    else {
      dy[i] = 0.5*(ynew[i + 1] - ynew[i - 1]);
    }
  }

  free(z);
}


void DetectNegativeness(double *xnew, double *ynew, double *dy, double *x, double *dx, 
			double *h, double *y, double *X, double *Y, int *binTag, 
			int numDataPoints, int numIntevals, int numSubIntervals)
{

  for(int i = 0; i < numSubIntervals; i++) {    
    // for each sub (half) interval, compute the minumum

    double yL = ynew[i];
    double yR = ynew[i + 1];
    
    double syL = dy[i];
    double syR = dy[i + 1];

    // finds "u" that makes y'(u) = 0, then compare it with
    // the two end knots to see which one is the lowest point.
    // y'(u) = 0 is a quadratic so can be solved analytically
    // so now define the coefficients of the quadratic f'(u) = 0.
  
    // the coefficient for A u^2
    double A = 6*yL + 3*syL - 6*yR + 3*syR;
    
    // the coefficient for 2B u^1
    double B = - 3*yL - 2*syL + 3*yR - syR;

    // the coefficient for C u^0
    double C = syL;

    double root[2];

    int numRoots = 0; // assume no roots

    if(fabs(A) > MICRO) {
      // scale again
      B /= A; C /= A; A = 1.0;
      
      double delta = pow(B, 2) - C;
      
      if(delta < 0.0) {
	// no real critical point, slope will not change sign between (0, 1)
	// also by choice all knots are on or above y = 0, so no negativeness.
	numRoots = 0;
      }
      else {
	// two critical points exist
	// here real roots exist, the two roots are
	delta = sqrt(delta);
	
	root[0] = - B - delta;
	root[1] = - B + delta;
	numRoots = 2;

	if(root[0] < 0.0 || root[0] > 1.0) {
	  root[0] = root[1];
	  numRoots--;
	}

	if(root[1] < 0.0 || root[1] > 1.0) {
	  numRoots--;
	}
      }    
    }
    else {
      // probably not quadratic but linear
      root[0] = - C / (B + B) ;

      if(0.0 <= root[0] && root[0] <= 1.0) {
	numRoots = 1;
      }
    }

    int numNegatives = 0;

    if(numRoots > 0) {
      // check if the root is a minimum
      for(int count = 0; count < numRoots; count++) {
	double det = A*root[count] + B;
	if(det >= 0) {
	  // possibly a minimum, check if negative
	  double u = root[count];
	  double value = 
	    yL*H00(u) + syL*H10(u) + yR*H01(u) + syR*H11(u); 
	  if(value < 0) {
	    root[numNegatives] = root[count];
	    numNegatives++;
	  }
	}
      }
    } // end checking negatives

    if(numNegatives == 1) {
      // one negative, perform the treatment.
      // i am ok to do this for the negative is assumed isolated
      // then the treatment is definitely local.
    
      int originalBinID = (int) (i/2);
      binTag[originalBinID] = 0; // negativeness treated in this bin
    }
    else if(numNegatives == 2) {
      printf(" strange case! \n");
      exit(0);
    }
  }
}


// update sum of areas with new control points
void AreaSumUpdate(double *xnew, double *ynew, double *snew, double *dy,
		   double *x, double *dx, double *h, int *binTag, 
		   int numDataPoints, int numIntervals, int numSubIntervals)
{
  snew[0] = 0.0;

  // accumulated count at knots
  double s0 = 0.0;

  for(int i = 0; i < numSubIntervals; i++) {

    double yL = ynew[i];
    double yR = ynew[i + 1];

    double syL = dy[i];
    double syR = dy[i + 1];

    double area = (yL*I00 + syL*I10 + yR*I01 + syR*I11)*(xnew[i + 1] - xnew[i]);
    s0 += area;
    snew[i + 1] = s0;
  }
}



// back solve for even bin count in each new bin
void BackSolveNewBins(double *xnew, double *ynew, double *snew, double *dy, double *s, 
		      double *x, double *dx, double *h, double *xstar, int*binTag, 
		      int numDataPoints, int numIntervals, int numSubIntervals)
{
  // M is desired number of bins
  int M = numDesiredBins;
  
  // N the number of new knots
  int N = M + 1;

  // the desired counts each bin
  double ds0 = s[numIntervals] / ((double) M);
  
  // the aaccumulated counts
  double s0 = 0.0;

  // the left end alignment
  xstar[0] = xnew[0];

  int knotID = 1;
  int binID = 0;

  double uOld = 0.0;

  int startBin = 0;

  // search for even area increasing points 
  for(int i = 1; i < M; i++) {
    // the desired accumulated count
    s0 += ds0;
    
    // search for the solution bin of "s0"
    for(int j = startBin; j < numSubIntervals; j++) {
      if(snew[j] < s0 && s0 <= snew[j + 1]) {
	xstar[i] = KnotLocation(xnew, ynew, snew, dy, s0, j);
	startBin = j;
      }
    }
  }

  xstar[M] = x[numIntervals];
}


double KnotLocation(double *xnew, double *ynew, double *snew, double *dy, 
		    double s0, int i)
{
  // left knot of the sub-bin "i" and slope
  double xL = xnew[i]; 
  double yL = ynew[i];
  double dyL = dy[i];

  // right knot of the sub-bin "i" and slope
  double xR = xnew[i + 1];
  double yR = ynew[i + 1];
  double dyR = dy[i + 1];

  // width of bin
  double dx = (xR - xL); 
 
  // area to match  
  double areaToMatch = s0 - snew[i];

  // initial guess
  double u = areaToMatch / (snew[i + 1] - snew[i]);


  // to integrate the Hermite interpolant to "u" to match area
  // the function: yL H00(u) + dyL H10(u) + yR H10(u) + dyR H11(u)
  // since the function is monotone, it is safe with any method
  // i try my (??) accelerated Newton's scheme

  double f = MEGA;
  double diff = MEGA;
  int iter = 0;

  while (iter < 36 && diff > PICO) {
    f = 
      dx*(yL*K00(u) + dyL*K10(u) + yR*K01(u) + dyR*K11(u)) - areaToMatch;
    double df = dx*(yL*H00(u) + dyL*H10(u) + yR*H01(u) + dyR*H11(u));
    double up = u - f / df;
    
    f += dx*(yL*K00(up) + dyL*K10(up) + yR*K01(up) + dyR*K11(up)) - areaToMatch;
    double du = - f / df;
    u += du;

    diff = MIN(fabs(du), fabs(f));
    iter++;
  }

  return (xL + dx*u);
}


//////////////////////////////////////////////////////////////////////////
// TreatNegativeness()
// treat isolated interior negativeness
// algorithm: delete the control point at the middle, then there are
//            three area match conditions and a nonnegative condition
//            to solve positions of the four neighbor control points.
// author:    JIN YAO
///////////////////////////////////////////////////////////////////////////
void TreatNegativeness(int subBinID, double *xnew, double *ynew, double *dy, double *h)
{
  // first figure out the original bin ID
  int i = subBinID / 2;
  int shift = subBinID - 2*i;

  // read in current control points that violates positiveness

  int iL = subBinID - shift;
  int iLL = iL - 1;
  int iLLL = iLL - 1;
  int iLLLL = iLLL + 1;

  int iM = iL + 1;

  int iR = iL + 2;
  int iRR = iR + 1;
  int iRRR = iRR + 1;
  int iRRRR = iRRR + 1;

  // position of Y at the local control points
  
  double yLLLL = ynew[iLLLL];
  double yLLL = ynew[iLLL];
  
  double yRRR = ynew[iRRR];
  double yRRRR = ynew[iRRRR];

  // level in each bin involved
  double hM = h[i]; double hL = h[i - 1]; double hR = h[i + 1];

  // fix control points in the middle bin
  double yM = hM; double yL = hM; double yR = hM;

  // modify slope for negativeness treatment 
  dy[iL] = 0.0; dy[iM] = 0.0; dy[iR] = 0.0;
  
  double yLL = 2*hL - HALF*(yLLL + yL) - dy[iLLL] / 12.0;
  double yRR = 2*hR - HALF*(yRRR + yR) + dy[iRRR] / 12.0;
  
  // update the positions of control points regarding keeping positivity.
  ynew[iLL] = yLL;
  ynew[iL] = yL;
  ynew[iM] = yM;
  ynew[iR] = yR;
  ynew[iRR] = yRR;

  // update the slope
  dy[iLL] = HALF*(yL - yLLL);
  dy[iL] = 0.0; 
  dy[iM] = 0.0; 
  dy[iR] = 0.0;
  dy[iRR] = HALF*(yRRR - yR);
}


void FillIntArray(int *array, int N, int value)
{
  for(int n = 0; n < N; n++) {
    array[n] = value;
  }
}


void FillDoubleArray(double *array, int N, double value)
{
  for(int n = 0; n < N; n++) {
    array[n] = value;
  }
}


// plot the input bin data
void PlotInput(double *x, double *h, double *s, int numDataPoints)
{
  FILE *plotPtr;
  plotPtr = fopen("inputBinCounts.dat", "w");
  
  int numIntervals = numDataPoints - 1;

  for(int i = 0; i < numIntervals; i++) {
    fprintf(plotPtr, " %f %f \n", x[i], h[i]);
    fprintf(plotPtr, " %f %f \n", x[i + 1], h[i]);
  }

  fclose(plotPtr);

  plotPtr = fopen("accumulatedCounts.dat", "w");
  
  for(int i = 0; i < numDataPoints; i++) {
    fprintf(plotPtr, " %f %f \n", x[i], s[i]);
  }

  fclose(plotPtr);
}


// plot the area matched hermit spline and the newly binned data
void PlotSolution(double *xnew, double *ynew, double *dy,  
		  int numControlPoints, int numSubIntervals)
{
  FILE *dataPtr;
  dataPtr = fopen("AreaMatchedHermitSpline.dat", "w");

  for(int i = 0; i < numSubIntervals; i++) {
    
    double xL = xnew[i];
    double yL = ynew[i];

    double xR = xnew[i + 1];
    double yR = ynew[i + 1];

    double syL = dy[i];
    double syR = dy[i + 1];

    for(int j = 0; j < 20; j++) {
      
      double u = 0.05*j;
      double xC = xL + u*(xR - xL);
      double yC = yL*H00(u) + syL*H10(u) + yR*H01(u) + syR*H11(u);
	
      fprintf(dataPtr, " %f %f \n", xC, yC);
    }
  }

  fclose(dataPtr);

  dataPtr = fopen("controlPoints.dat", "w");
  // plot control points
  for(int i = 0; i < numControlPoints; i++) {
    fprintf(dataPtr, "%f %f \n", xnew[i], ynew[i]);
  }

  fclose(dataPtr);
}


// plot the newly binned data
void PlotNewBinData(double *xstar, double ds0, int M)
{

  FILE *dataPtr;
  dataPtr = fopen("newBins.dat", "w");
  
  for(int i = 0; i < M; i++) {
    double height = ds0 / (xstar[i + 1] - xstar[i]);
    fprintf(dataPtr, " %f %f \n", xstar[i], height);
    fprintf(dataPtr, " %f %f \n", xstar[i + 1], height);
  }

  fclose(dataPtr);
}

void TriDiagonalSolver(double *x, const double *a, const double *b, const double *c, const int N) 
{  
  // Allocate scratch space. 
  double *cp = (double *) malloc(sizeof(double) * N);
  
  cp[0] = c[0] / b[0];
  x[0] = x[0] / b[0];
  
  /* loop from 1 to N - 1 inclusive */
  for (int in = 1; in < N; in++) {
    double t = 1.0 / (b[in] - a[in - 1] * cp[in - 1]);
    cp[in] = c[in] * t;
    x[in] = (x[in] - a[in - 1] * x[in - 1]) * t;
  }
  
  /* loop from N - 2 to 0 inclusive, safely testing loop end condition */
  for (int in = N - 1; in-- > 0; )
    x[in] = x[in] - cp[in] * x[in + 1];
  
  /* free scratch space */
  free(cp);
}
