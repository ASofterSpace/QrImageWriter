# QR Image Writer

**Class:** Utility

**Language:** Java

**Platform:** Windows / Linux

Writes QR codes into images, also adjusting the images in the process

## Setup

Download our Toolbox-Java (which is a separate project here on github) into an adjacent directory on your hard drive.

Start the build by calling under Windows:

```
build.bat
```

Or under Linux:

```
build.sh
```

## Run

Before running the QR Image Writer, you should put the image that you want to use as input into the same folder as `.jpg`, as `.bmp` or as `.ppm` file.

You should then create a file called `input.json` containing info such as e.g.:

```
{
	"baseurl": "http://www.asofterspace.com/",
	"id": "index",
	"picture": "input.jpg",
	"logo": "logo.jpg",
	"width": 50,
	"height": 40
}
```

To now start up the QrImageWriter project after it has been built, you can call under Windows:

```
run.bat
```

Or under Linux:

```
run.sh
```

After running, the output will be saved to `output.ppm`.

## License

We at A Softer Space really love the Unlicense, which pretty much allows anyone to do anything with this source code.
For more info, see the file UNLICENSE.

If you desperately need to use this source code under a different license, [contact us](mailto:moya@asofterspace.com) - I am sure we can figure something out.
