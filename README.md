# RADSIM

RADSIM is an open-source simulation framework that will provide the capability to: (1) simulate radiation source emissions, (2) interpolate results from radiation transport tools into a common format in order to prepare incident flux, and (3) model radiation detector output from incident flux to rapidly produce synthetic radiation measurement templates with typically encountered variations in detector response.

## Getting Started

### Prerequisites

```
Python3.x
Java (JDK11+)
Jpype (Python-Java bridge)

```

### Installing

This helper repository pulls together all the standard tools and packages used by RadSim.

Note!  You will need to have [JPype](https://jpype.readthedocs.io/en/latest/install.html) installed as part of your python environment.

Building from Source
--------------------
Once you have cloned the RadSim repository, enter the command below to create the jars
```
	python build.py jar
```

An example script on how to output a list of emissions and intesities for a given collections of sources and respective ages is provided in gov.llnl.rtk/py/ subdirectory. Run the api-test.py to obtain a sample list of emission energies and intensities.
```
	python api-test.py
```

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* Karl Nelson
* Dhanush Hangal
* Vincent Cheung
* Bonnie Canion
* Simon Labov
* Caleb Mattoon

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details

## Acknowledgments

* This work was supported by the Office of Defense Nuclear Nonproliferation Research and Development
within the U.S. Department of Energy’s National Nuclear Security Administration. 

* This work was produced under the auspices of the U.S. Department of Energy by
Lawrence Livermore National Laboratory under Contract DE-AC52-07NA27344.

* LLNL-CODE-854710 LLNL-CODE-855199
