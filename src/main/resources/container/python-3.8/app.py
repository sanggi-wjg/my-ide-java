import sys
from io import StringIO

from flask import Flask, request, jsonify

app = Flask(__name__)


@app.route("/", methods = ['POST'])
def run_code():
    code_output = StringIO()
    code_error = StringIO()
    sys.stdout = code_output
    sys.stderr = code_error

#     code = request.form.get('code', '')
    body = request.get_json()
    code = body.get('code', '')
    # print(body)
    # print(code)

    try:
        exec(code)
    except Exception as e:
        import traceback
        # exc_type, exc_value, exc_tb = sys.exc_info()
        trace = traceback.format_exc().replace('Traceback (most recent call last):\n   ', '')
        trace = trace.replace("""File \".\\app.py\", line 23, in run_code\n   """, '')
        trace = trace.replace('exec(code)\n  ', '')
        sys.stderr.writelines(f"{trace}")

    sys.stdout = sys.__stdout__
    sys.stderr = sys.__stderr__
    # print(f"error: {code_error.getvalue()}")
    # print(f"output: {code_output.getvalue()}")
    return { 'output': code_output.getvalue(), 'error': code_error.getvalue() }


if __name__ == '__main__':
    app.debug = True
    app.run()