# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html). (Patch version X.Y.0 is implied if not specified.)

## [Unreleased](https://github.com/NWQMC/WQP-WQX-Services/compare/WQP-WQX-Services/compare/WQP-WQX-Services-1.6.0...master)
## Changed
-   Performance improvement on searches when specifying project
-   Increase work_mem on result queries to reduce timeouts

## [1.6.0](https://github.com/NWQMC/WQP-WQX-Services/compare/WQP-WQX-Services/compare/WQP-WQX-Services-1.6.0...master) - 2020-07-13
### Added
-   Redirect from ../swagger to the correct url for the Swagger UI

### Changed
-   Multiple Deploys

## [1.5.0](https://github.com/NWQMC/WQP-WQX-Services/compare/WQP-WQX-Services/compare/WQP-WQX-Services-1.4.0...WQP-WQX-Services-1.5.0)
## Changed
-  Healthcheck specified in pipeline.yml now points to data/about/health

## [1.4.0](https://github.com/NWQMC/WQP-WQX-Services/compare/WQP-WQX-Services/compare/WQP-WQX-Services-1.3.0...WQP-WQX-Services-1.4.0)
### Changed 
-   Dockerfile now uses debin-stretch-openjdk-11 which is the latest rather than a specific tagged version

## [1.3.0](https://github.com/NWQMC/WQP-WQX-Services/compare/WQP-WQX-Services/compare/WQP-WQX-Services-1.2.0...WQP-WQX-Services-1.3.0)
### Added
-   Automated Database Integration tests
-   Code Coverage reporting

### Changed
-   Spring-Boot-Starter > 2.2.0

## [1.2.0](https://github.com/NWQMC/WQP-WQX-Services/compare/WQP-WQX-Services-1.1.0...WQP-WQX-Services-1.2.0)
### Changed
-   Updated Jenkins build and deploy pipeline and configuration to use a new pipeline library

## [1.1.0](https://github.com/NWQMC/WQP-WQX-Services/compare/WQP-WQX-Services-1.0.0...WQP-WQX-Services-1.1.0)
### Fixed
-   Fixed problems with queries timing out

### Changed 
-   Url mappings where changed to /data

## [1.0.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.20.0...WQP-WQX-Services-1.0.0)
### Changed
-   Convert to PostgreSQL.

## [0.20.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.15.0...wqp-0.20.0)
### Added
-   ActivityGroupURL to the activityAll dataProfile.
-   resultPhysChem dataProfile.
-   resultPrimary dataProfile.
-   resultBroad dataProfile.
-   added organization summary.
-   added period of record data profile with CSV output.
-   added Biological Metric data profile

### Changed
-   Station summary service returns only sites that have summary information.
-   Fixed ActivityFileUrl, ActivityMetricURL, and ActivityAttachedBinaryObject logic errors.
-   Refactored Summary Station mapper to use new format of station_sum table.
-   Changed use of 'summarystation' to summaryMonitoringLocation.' 

## [0.15.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.14.0...wqp-0.15.0)
### Added
-   Added station summary service

### Fixed
-   Post bodies now correctly decode 'pCode' and 'bBox'
-   Removed the use_hash hint for geospatial result queries

## [0.14.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.13.0...wqp-0.14.0)
### Added
-   Converted project to Spring Boot 2.
-   OAuth2 Security for Internal App.

## [0.13.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.12.0...wqp-0.13.0) - 2018-03-13
### Changed
-   Fixed issue with inappropriate parameters being applied to retrieval.
-   Refactor of tests in support of this bug fix.

## [0.12.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.11.0...wqp-0.12.0) - 2018-02-22
### Added
-   Project Monitoring Location Weighting Download.

## [0.11.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.10.0...wqp-0.11.0) - 2018-02-16
### Added
-   Project, Monitoring Location, Activity, and Result Attachement Downloads.

### Changed
-   Ignore empty/null parameters.

## [0.10.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.9.0...wqp-0.10.0) - 2018-01-25
### Added
-   Project Data Download

### Changed
-   Parameter handling refactor.

## [0.9.0](https://github.com/NWQMC/WQP-WQX-Services/compare/wqp-0.8.0...wqp-0.9.0) - 2017-12-15
### Added
-   This changelog file.

## 0.8 - 2017-11-29
### Changed
-   Migrated repository to NWQMC
