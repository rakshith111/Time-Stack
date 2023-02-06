# Configuration file for the Sphinx documentation builder.
#
# For the full list of built-in configuration values, see the documentation:
# https://www.sphinx-doc.org/en/master/usage/configuration.html

# -- Project information -----------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#project-information
import sys
import os
project = 'Time Stack'
copyright = '2023, rakshith111, MAX-dev2020'
author = 'rakshith111, MAX-dev2020'
release = 'Prototype'

# -- General configuration ---------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#general-configuration


p = sys.path.insert(0, os.path.abspath('../../'))

templates_path = ['_templates']
html_static_path = ['_static']

exclude_patterns = []
# add mock imports since we don't have the dependencies installed when building docs and we have imported modules in the same folder
autodoc_mock_imports = ["ui"]

# add the extensions
extensions = ['sphinx.ext.autodoc', 'sphinx.ext.viewcode',
              'myst_parser']
html_theme = 'furo'
pygments_style = "sphinx"
pygments_dark_style = "monokai"

# add __init__ to the folder of files to add into documentation
autoclass_content = 'both'

# -- Options for HTML output -------------------------------------------------
# https://www.sphinx-doc.org/en/master/usage/configuration.html#options-for-html-output
