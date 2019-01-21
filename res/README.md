# Transformation Rules 
These are transformation rules for OpenGDS/Conversion.
Following transformation rules were made by Hale Studio.

All files are named according to the rules below:
```
[SourceFormat] (Version) (Module name) To [TargetFormat] (Version) (Module name) (_ValidGeometry).halez
```
 * SourceFormat: GIS format name (i.e. CityGML, IndoorGML and GML)
 * Version: GIS format version (i.e. IndoorGML103 is IndoorGML 1.0.3)
 * Module name: Module name of GIS format (i.e. CityGML2Building is Building module of CityGML)
 * ValidGeometry: This is a special rule for making (or using) valid Hale studio geometry

For example, CityGML2ToGML.halez is a transformation rule from CityGML 2.0 to GML (SimpleFeature).