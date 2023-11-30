import time as _tm

# Very loosely based off of
#   http://stackoverflow.com/questions/5849800/tic-toc-functions-analog-in-python
_startTime=0
_timeList=[]

def tic():
    #Homemade version of matlab tic and toc functions
    global _startTime
    global _timeList
    start=_tm.time()
    if len(_timeList)==0:
        _startTime=start
    _timeList.append(start)

def toc():
    now=_tm.time()
    global _startTime
    global _timeList
    if _startTime==0:
        print("Tic not called")
        return
    start=_startTime
    if len(_timeList)>0:
        start=_timeList.pop()
    print("elapsed %f s"%(now-start))

