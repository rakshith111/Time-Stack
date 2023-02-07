import subprocess
import os
import shutil
def make_python():

    subprocess.call(["docs-builder\\make.bat","html"], shell=True)


def make_android():

    os.chdir("Android-app\\TimeStackPrototype")
    subprocess.call(["gradlew.bat","dokkaHtml"], shell=True)
    os.chdir("..\\..")


def move_docs():

    subprocess.call(["git", "add", "docs"])
    subprocess.call(["git", "commit", "-m", "Update docs"])
    subprocess.call(["git", "push"])

    # Check out the docs branch
    subprocess.call(["git", "checkout", "docs"])
    # Move the docs folder to the root of the repository
    subprocess.call(["git","checkout","prototype","--","docs"])
    subprocess.call(["git", "add", "docs"])
    subprocess.call(["git", "commit", "-m", "Update docs"])
    subprocess.call(["git", "push"])

make_python()
make_android()
move_docs()
