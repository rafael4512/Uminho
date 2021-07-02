import time
# Print iterations progress
def printProgressBar (iteration, total, prefix = '', suffix = '', decimals = 1, length = 100, fill = '█', printEnd = "\r"):
    """
    Call in a loop to create terminal progress bar
    @params:
        iteration   - Required  : current iteration (Int)
        total       - Required  : total iterations (Int)
        prefix      - Optional  : prefix string (Str)
        suffix      - Optional  : suffix string (Str)
        decimals    - Optional  : positive number of decimals in percent complete (Int)
        length      - Optional  : character length of bar (Int)
        fill        - Optional  : bar fill character (Str)
        printEnd    - Optional  : end character (e.g. "\r", "\r\n") (Str)
    """
    percent = ("{0:." + str(decimals) + "f}").format(100 * (iteration / float(total)))
    filledLength = int(length * iteration // total)
    bar = fill * filledLength + '-' * (length - filledLength)
    print(f'\r{prefix} |{bar}| {percent}% {suffix}', end = printEnd)
    # Print New Line on Complete
    if iteration == total: 
        print()
#Bar sample 
def bar_example():
    ite=100 #numero de interações do ciclo
    
    # Initial call to print 0% progress
    printProgressBar(0, ite, prefix = 'Progress:', suffix = 'Complete', length = 10)
    for i in range(1,ite+1):
        # Do stuff...
        time.sleep(0.1)
        printProgressBar(i , ite, prefix = 'Progress:', suffix = 'Complete', length = 10)


def showbar(ite,max_ite):
    printProgressBar(ite, max_ite-1, prefix = 'Progress:', suffix = 'Complete', length = 50)
