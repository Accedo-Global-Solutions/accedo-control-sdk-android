## Changelog ##

v.1.3.1 (2023-12-11)

* Same as 1.3.0

v.1.3.1-kotlin.1.7.10 (2023-12-11)

* Force Kotlin 1.7.10 compatibility
* Downgrade OkHttp to 4.11.0

v.1.3.0 (2023-12-04)

* Add support for Custom Conditions to be used in whitelisting
* Updated OkHttp to 4.12.0

v1.2.2 (2023-10-09)

* Updated AndroidX Annotations to 1.7.0
* Updated OkHttp to 4.11.0

v1.2.1 (2022-11-21)

* Added nullable annotations

v1.2.0 (2022-06-14)

* Removed HttpUrlConnection and added OkHttp3 dependency

v1.1.2 (2022-06-24)

* Save timestamp only if write was successful.

v1.1.1 (2021-02-04)

* Catching NullPointer in SessionParser

v1.1.0 (2020-11-XX)

* Renamed one-sdk module to control-sdk
* Updated gradle tools version to 3.6.4
* Upgraded gradle wrapper to 6.6.1
* Upgraded compile and target versions to 29

v1.0.3 (2020-06-09)

* Developers now are able to attach an onResponseListener to their AccedoOneImpl subclass.

v1.0.2 (2020-03-25)

* Added explicit GMT server time handling for If-Modified-Since fixing locale issues in some regions.

v1.0.1 (2019-12-05)

* Fixing an issue where device time was used for If-Modified-Since headers on certain devices, instead of server time.

v1.0.0 (2017-12-20)

* Initial release, based on the VDK Appgrid client, v3.4.1 (2017-11-09)
