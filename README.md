# image-uploader
[![Build Status](https://travis-ci.org/Avexis/image-uploader.svg?branch=master)](https://travis-ci.org/Avexis/image-uploader)

[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=avexis-image-uploader)](https://sonarcloud.io/dashboard/index/avexis-image-uploader)

[![Coverage Status](https://coveralls.io/repos/github/Avexis/image-uploader/badge.svg?branch=master)](https://coveralls.io/github/Avexis/image-uploader?branch=master)

A module for storing images in multiple resolutions

#### ImageUploader

Main class that recieves images and controlls the flow of the transformers and stores.

#### ImageTransformer

Can be injected.
Defined per extension. Resizes and converts BufferedImages to base64.

#### ImageStorer

Can be injected.
Responsible for storing an BufferedImage. LocalImageStorer saves the BufferedImage to a defined directory.

##### ImageUploaderFactory - FilenameFormat
Uses String.format for variable injections.<br/>

Position | Value | Selector | Description
--- | --- | --- | ---
1 | filename | %1$s | original filename
2 | width | %2$s | real width of image
3 | height | %3$s | real height of image
4 | size name | %4$s | the given name for the size name
5 | extension | %5$s | file extension (jpg, png, ...)

#### Example

filenameFormat | Result
--- | ---
%1$s_%2$s_%3$s_%4$s.%5$s | myimage_1920_1080_large.jpg
%1$s_%4$s_%2$s_%3$s.%5$s | myimage_large_1920_1080.jpg

## License
MIT
