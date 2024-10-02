import jpype.imports
if not jpype.isJVMStarted():
    jpype.startJVM(
        'C:\\Users\\lahmann1\\.jdks\\openjdk-21\\bin\\server\\jvm.dll',
        classpath=[
            'C:\\Users\\lahmann1\\devel\\RadSim\\jars\\*',
            'C:\\Users\\lahmann1\\IdeaProjects\\radsim-mcnp-api\\jars\\*'
        ]
    )