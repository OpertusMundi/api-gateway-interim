CREATE SCHEMA IF NOT EXISTS "file"; 

--
-- File types
--
DROP TABLE IF EXISTS "file".asset_file_type CASCADE;
DROP SEQUENCE IF EXISTS "file".asset_file_type_id_seq CASCADE;

CREATE SEQUENCE "file".asset_file_type_id_seq INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

CREATE TABLE "file".asset_file_type
(
  "id"                              integer                 NOT NULL   DEFAULT nextval('file.asset_file_type_id_seq'::regclass),
  "category"                        character varying(60)   NOT NULL,
  "format"                          character varying(60)   NOT NULL,
  "extensions"                      character varying,
  "allow_bundle"                    boolean                 NOT NULL,
  "bundle_extensions"               character varying,
  "enabled"                         boolean                 NOT NULL,
  "notes"                           character varying,
  CONSTRAINT pk_asset_file_type PRIMARY KEY (id)
);

--
-- Initialize default values
--

insert into "file".asset_file_type ("category", "format", "extensions", "allow_bundle", "bundle_extensions", "enabled", "notes") values
('VECTOR', 'ESRI shapefile', 'shp',     true,  null, true, 'Must be accompanied with at least .dbf and .shx; optional other files like .prj may also be part of a shapefile'),
('VECTOR', 'CSV',            'csv,txt', false, null, true,  'Comma separated values; different separators may be used'),
('VECTOR', 'GeoJSON',        'geojson', false, null, true,  null),
('VECTOR', 'GML',            'gml',     false, null, true,  'Geography Markup Language'),
('VECTOR', 'KML',            'kml,kmz', false, null, true,  'Keyhole Markup Language'),
('VECTOR', 'XLS',            'xls',     false, null, true,  'MS Excel format'),
('VECTOR', 'XLSX',           'xlsx',    false, null, true,  'MS Office Open XML spreadsheet')
;

insert into "file".asset_file_type ("category", "format", "extensions", "allow_bundle", "bundle_extensions", "enabled", "notes") values
('RASTER', 'GTiff',          'tif,tiff,tff',  true,  null, true,  'GeoTIFF File Format; usually accompanied with a world file with extension .tfw, .tifw/.tiffw or .wld'),
('RASTER', 'COG',            'tif,tiff,tff',  true,  null, true,  'Cloud Optimized GeoTIFF generator; usually accompanied with a world file with extension .tfw, .tifw/.tiffw or .wld'),
('RASTER', 'HFA',            'img',           false, null, true,  'Erdas Imagine'),
('RASTER', 'USGSDEM',        'dem',           false, null, true,  'USGS ASCII DEM (and CDED)'),
('RASTER', 'AAIGrid',        'asc',           false, null, true,  'Arc/Info ASCII Grid'),
('RASTER', 'AIG',            'adf',          true,  null, true,  'Arc/Info Binary Grid (typically several .adf files in a common directory; possibly accompanied with .prj)'),
('RASTER', 'MrSID',          'sid',           true,  null, true,  'Multi-resolution Seamless Image Database; usually accompanied with a world file .sdw')
;

insert into "file".asset_file_type ("category", "format", "extensions", "allow_bundle", "bundle_extensions", "enabled", "notes") values
('NETCDF', 'netCDF',         'nc',      false, null, true, 'NetCDF: Network Common Data Form')
;
