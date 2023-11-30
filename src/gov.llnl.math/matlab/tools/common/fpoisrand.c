/*
 * =============================================================
 * xtimesy.c - example found in API guide
 *
 * Multiplies an input scalar times an input matrix and outputs a
 * matrix. 
 *
 * This is a MEX-file for MATLAB.
 * Copyright (c) 1984-2000 The MathWorks, Inc.
 * =============================================================
 */

/* $Revision: 1.10 $ */

#include "mex.h"
#include <math.h>
//************************************************************

// DRAND port
typedef __int64 long64;
static const long64 a_seed_=0x0005deece66di64;
static const long64 c_seed_=0x00000000000bi64;
static long64 r_=0x330e, a_=0x0005deece66di64, c_=0x0000000000bi64; 
unsigned short sd_[3]  = {0,0,0};

#define LL2US(X, Y) {Y[0]=(unsigned short)X; Y[1]=(unsigned short)(X>>16); Y[2]=(unsigned short)(X>>32);}
#define US2LL(Y, X) {X=(*(Y+2)<<16)|*(Y+1); X<<=16; X|=*Y;}
unsigned short* _seed48(unsigned short sd[3])
{
  a_=a_seed_;
  c_=c_seed_;
  LL2US(r_, sd_)
  US2LL(sd,r_)
  return sd_;
}

void _lcong48(unsigned short sd[7])
{
  US2LL(sd,r_)
  US2LL(sd+3,a_)
  c_=sd[6];
}

void _srand48(long seed)
{
  unsigned short sd[3];
  sd[0]=0x330e;
  sd[1]=seed&0xffff;
  sd[2]=(seed>>16)&0xffff;
  _seed48(sd);
}

long _jrand48(unsigned short sd[3])
{
  long64 r;
  US2LL(sd,r)
  r=(a_*r + c_)&0xffffffffffffi64;
  LL2US(r,sd)
  return (long)(r>>16);
}

long _nrand48(unsigned short sd[3])
{
  return _jrand48(sd)>>1;
}

long _mrand48()
{
  r_=(a_*r_ + c_)&0xffffffffffffi64;
  return (long)(r_>>16);
}

long _lrand48()
{
  return _mrand48()>>1;
}

double _drand48()
{
  double rv;
  _mrand48();
  rv=(double)(r_);
  rv/=281474976710656.0;
  return rv;
}

double _erand48(unsigned short sd[3])
{
  double rv;
  long64 r;
  _jrand48(sd);
  US2LL(sd, r);
  rv=(double)(r);
  rv/=281474976710656.0;
  return rv;
}
// end DRAND
//
// **************************************************************



// Generate a random number from a Poisson distribution with the given mean
//   by counting the number of multiplied independent draws from a uniform
//   PRNG needed to be less than exp(-m)
//
// This is based upon the observation that the Poisson process can be viewed
//   as a sequence of exponential inter-arrival times, and thus we could count
//   the number of exponential random-draws summed to exceed the mean (m) --
//   this is then converted to the multiplication of uniform draws compared
//   to exp(-m)
//
// The generator is linear in the parameter m.
//
static unsigned int cumexp_poisson_prng(double lambda)
{
  double g = exp(-lambda);
  double cumprod = _drand48();
  unsigned int num_mults = 0;
  while (cumprod > g)
  {
    cumprod *= _drand48();
    num_mults++;
  }
  return num_mults;
}


//****************************************************************

// Helper function for precomputed log(x!)
static double logfac(unsigned int x)
{
//  assert((x < 18) && "Logfac out of bounds");
  double table[18] = { 0, 0, 0.6931471806, 1.791759469, 
    3.178053830, 4.787491743, 6.579251212, 8.525161361,
    10.60460290, 12.80182748,
    15.104412573, 17.502307846, 19.987214496, 22.552163853,
    25.191221183, 27.899271384, 30.671860106, 33.505073450  };

  if (x>17) 
    return 0;

  return table[x];
}

// Based on the Hormann transformational rejection generator (1993) for the
//   Poisson distribution; it appears to be both fast and "uniformly fast"
//   (see http://citeseer.csail.mit.edu/151115.html; PTRD algorithm)
//   (on average, appears to be at least twice as fast as the simple
//    cumulative exponential generator)
//
// (sorry for the magic constants throughout, but they're approximations that
//  help speed things up)
//
// Note that approximations are generally good for lambda > 10
//
static double hormann_poisson_prng(double lambda)
{

  // Note that the lambda value has been floor()'d by the type system
  double smu = sqrt(lambda);
  double b = 0.931 + 2.53*smu;
  double a = -0.059 + 0.02483*b;
  double inv_alpha = 1.1239 + 1.1328/(b-3.4);
  double vr = 0.9277 - 3.6224/(b-2.0);
  double us;
  double stirling_etc;
  double log_lambda=log(lambda);
  double magic=0.9189385332046727; // log(sqrt(2*3.1415926535897931));
  double k;

  while (1)
  {
    double U;
    double v = _drand48();
    if (v < 0.86*vr)
    {
      U = v/vr - 0.43;
      return floor((2.0*a/(0.5 - (U<0?-U:U)) + b)*U + lambda + 0.445);   
    }
    if (v > vr)
    {
      U = _drand48() - 0.5;
    }
    else
    {
      U = v/vr - 0.93;
      U = (U<0?-1:1)*0.5 - U;
      v = _drand48() * vr;
    }
    us = 0.5 - (U<0?-U:U);

    if ((us < 0.013) && (v > us))
      continue;   // continue big loop by going back to step 1

    k = floor((2.0*a/us + b)*U + lambda + 0.445);

    v *= inv_alpha/(a/(us*us) + b);

    stirling_etc = (k + 0.5)*log(lambda/k)
      - lambda - magic + k
      - (1/12.0 - 1.0/(360*k*k))/k;     

    if ((k >= 18) && (log(v * smu) <= stirling_etc))
    {
      return k;
    }

    if ((k >= 0) && (k <= 17) &&
        (log(v) <= k * log_lambda - lambda - logfac(k)))
      return k;

  }   // wee, next try at the big loop... (the "rejection" part of the method)
}


//****************************************************************

void poisrand_m(double *x, double *z, int m, int n)
{
  int i,j,count = 0;
      
  for (i = 0; i < n*m; i++, z++, x++) 
  {
    if (*x<=0.0) 
      *z=0.0;
    else if (*x<40.0)
      *z=cumexp_poisson_prng(*x);
    else
      *z=hormann_poisson_prng(*x);
  }
}


/* The gateway routine */
void mexFunction(int nlhs, mxArray *plhs[],
                     int nrhs, const mxArray *prhs[])
{
  double *x, *z;
  int status,mrows,ncols;

  // seed commands
  if (nrhs && nrhs < 3 && mxIsChar(prhs[0]) )
  {
    char theString[10], *p = theString;
    if (mxGetString(prhs[0],theString,sizeof(theString))) 
       mexErrMsgIdAndTxt("fpoisrand:BadStringArg", "Invalid string arg.");
    for (;*p;p++) *p = tolower(*p); 
    if (strncmp(theString,"state",sizeof(theString)))
       mexErrMsgIdAndTxt("fpoisrand:BadStringArg", "Invalid string arg.");

    // set the seed
    if (nrhs==2)
    {
      mrows = mxGetM(prhs[1]);
      ncols = mxGetN(prhs[1]);
      if (mrows*ncols==1)
      {
        long u;
        x = mxGetPr(prhs[1]);
        u=(long)*x;
        _srand48(u);
      }
      else if (mrows*ncols==3)
      {
        unsigned short sd[3];
        x = mxGetPr(prhs[1]);
        sd[0]=(unsigned short)x[0];
        sd[1]=(unsigned short)x[1];
        sd[2]=(unsigned short)x[2];
        _seed48(sd);
      }
      else
      {
        mexErrMsgTxt("Bad seed.");
      }
    }

    // return the current seed
    if (nlhs>0)
    {
      plhs[0] = mxCreateDoubleMatrix(3,1, mxREAL);
      z = mxGetPr(plhs[0]);
      z[0]=sd_[0];
      z[1]=sd_[1];
      z[2]=sd_[2];
    }

    return;
  }
  
  if (nrhs != 1) 
    mexErrMsgTxt("One inputs required.");
  if (nlhs > 1) 
    mexErrMsgTxt("Too many output arguments.");
              
  /* Check to make sure the first input argument a double array */
  if (!mxIsDouble(prhs[0]) || mxIsComplex(prhs[0]) )
  {
     mexErrMsgTxt("Input x must be double.");
  }
                
  /* Create a pointer to the input matrix y. */
  x = mxGetPr(prhs[0]);
                    
  /* Get the dimensions of the matrix input y. */
  mrows = mxGetM(prhs[0]);
  ncols = mxGetN(prhs[0]);
                        
  /* Set the output pointer to the output matrix. */
  plhs[0] = mxCreateDoubleMatrix(mrows,ncols, mxREAL);
                          
  /* Create a C pointer to a copy of the output matrix. */
  z = mxGetPr(plhs[0]);
                            
  /* Call the C subroutine. */
  poisrand_m(x,z,mrows,ncols);
}
