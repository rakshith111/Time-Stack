import subprocess
import os
import shutil
def make_python():
    os.chdir(os.path.join(os.path.dirname(
        os.path.dirname(__file__)), "docs-builder", ))
    subprocess.run("make html", shell=True, check=True)


def make_android():
    os.chdir(os.path.join(os.path.dirname(
        os.path.dirname(__file__)), "Android-app", "TimeStackPrototype"))
    subprocess.run("gradlew.bat dokkaHtml", shell=True, check=True)
def move_docs():
    subprocess.call(["git", "add", "docs"])
    subprocess.call(["git", "commit", "-m", "Update docs"])
    subprocess.call(["git", "push"])

    docs_path= os.path.join(os.path.dirname(os.path.dirname(__file__)) , "docs")
    docs_move = os.path.join(os.path.dirname(
        os.path.dirname(os.path.dirname(__file__))), "docs")
    subprocess.run("xcopy /E /I /Y /Q "+docs_path+" "+docs_move, shell=True, check=True)

    # Check out the docs branch
    subprocess.call(["git", "checkout", "docs"])
    # Move the docs folder to the root of the repository
    subprocess.run("git checkout prototype -- docs ", shell=True, check=True)
    subprocess.run("git add docs", shell=True, check=True)
    subprocess.run("git commit -m 'Update docs'", shell=True, check=True)

make_python()
make_android()
move_docs()
