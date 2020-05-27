## Changelog ##

v1.0.3 (2020-05-27)

* Added setGlobalResponseListener() to Request, the underlying REST client of the SDK. Useful for analytics.

v1.0.2 (2020-03-25)

* Added explicit GMT server time handling for If-Modified-Since fixing locale issues in some regions.

v1.0.1 (2019-12-05)

* Fixing an issue where device time was used for If-Modified-Since headers on certain devices, instead of server time.

v1.0.0 (2017-12-20)

* Initial release, based on the VDK Appgrid client, v3.4.1 (2017-11-09)