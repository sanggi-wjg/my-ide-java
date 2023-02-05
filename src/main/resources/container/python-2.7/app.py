import sys
import Queue
from StringIO import StringIO


def run_code(code):
    code_output = StringIO()
    code_error = StringIO()
    sys.stdout = code_output
    sys.stderr = code_error

    try:
        exec code
    except Exception, e:
        import traceback
        trace = traceback.format_exc().replace('Traceback (most recent call last):\n  ', '')
        trace = trace.replace('File "/app/app.py", line 34, in run_code\n    ', '')
        trace = trace.replace('exec code\n  ', '')
        sys.stderr.writelines("%s" % trace)

    sys.stdout = sys.__stdout__
    sys.stderr = sys.__stderr__
    result_queue.put({ 'output': code_output.getvalue(), 'error': code_error.getvalue() })


code_queue = Queue.Queue()
result_queue = Queue.Queue()
if len(sys.argv) > 1:
    code_queue.put(''.join(sys.argv[1]))

if not code_queue.empty():
    run_code(code_queue.get())
    print result_queue.get()

