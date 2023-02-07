import subprocess
import os
import shutil
def make_python():

    subprocess.call(["docs-builder\\make.bat","html"], shell=True)


def make_android():

      subprocess.call(["Android-app\\TimeStackPrototype\\gradlew.bat","dokkaHtml"], shell=True)


def move_docs():

    subprocess.call(["git", "add", "docs"])
    subprocess.call(["git", "commit", "-m", "Update docs"])
    subprocess.call(["git", "push"])

    # Check out the docs branch
    subprocess.call(["git", "checkout", "docs"])
    # Move the docs folder to the root of the repository
    subprocess.run("git checkout prototype -- docs ", shell=True, check=True)
    subprocess.run("git add docs", shell=True, check=True)
    subprocess.run("git commit -m 'Update docs'", shell=True, check=True)

make_python()
make_android()
move_docs()
