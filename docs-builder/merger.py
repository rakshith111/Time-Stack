import subprocess
import os
import sys
def make_python():
    os.chdir(os.path.join(os.path.dirname(
        os.path.dirname(__file__)), "docs-builder", ))
    subprocess.run("make html", shell=True, check=True)


def make_android():
    os.chdir(os.path.join(os.path.dirname(
        os.path.dirname(__file__)), "Android-app", "TimeStackPrototype"))
    subprocess.run("gradlew.bat dokkaHtml", shell=True, check=True)

make_python()
make_android()