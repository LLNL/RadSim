This is python module for building RadSim project.  It is pulled by the .bootstrap.py
script at the top of the project.

To use this.

-  Copy .bootstrap in to the project.

-  Create a ``build.py`` script that starts with

.. code-block:: python

    #!/usr/bin/env python3

    # Bootstrap for a fresh pull
    exec(open(".bootstrap.py").read())

    # Import the project resources
    from project import *


