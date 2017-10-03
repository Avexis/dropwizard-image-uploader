# dropwizard-image-saver
[![Build Status](https://travis-ci.org/Avexis/dropwizard-image-saver.svg?branch=master)](https://travis-ci.org/Avexis/dropwizard-image-saver)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=avexis-dropwizard-image-saver)](https://sonarcloud.io/dashboard/index/avexis-dropwizard-image-saver)
[![Coverage Status](https://coveralls.io/repos/github/Avexis/dropwizard-image-saver/badge.svg?branch=master)](https://coveralls.io/github/Avexis/dropwizard-image-saver?branch=master)

A dropwizard module for storing images in multiple resolutions

## Configurations

Name | Type | Default | Description
--- | --- | --- | ---


#### FilenameFormat
Uses String.format for variable injections.<br/>
%1$s referes to String position 1: filename.

Position | Value | Selector | Description
--- | --- | --- | ---
1 | filename | %1$s | original filename
2 | width | %2$s | real width of image
3 | height | %3$s | real height of image
4 | size name | %4$s | the given name for the size name
5 | extension | %5$s | file extension (jpg, png, ...)
##### Example
filenameFormat: %1$s_%4$s_%2$s_%3$s.%5$s <br/>
Result could be: myimage_large_1920_1080.jpg


### Sample YML
```yaml
imageSaver:

```


## License
MIT
