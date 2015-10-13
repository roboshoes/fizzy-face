var gulp       = require( "gulp" );
var babelify   = require( "babelify" );
var browserify = require( "browserify" );
var connect    = require( "gulp-connect" );
var jade       = require( "gulp-jade" );
var sass       = require( "gulp-sass" );
var minifyCSS  = require( "gulp-minify-css" );
var gutil      = require( "gulp-util" );
var source     = require( "vinyl-source-stream" );
var buffer     = require( "vinyl-buffer" );

gulp.task( "scripts", function() {

    var bundler = browserify( "javascript/main.js", {
            debug: true
    } ).transform( babelify, {} );

    return bundler.bundle()
        .on( "error", gutil.log )
        .pipe( source( "main.js" ) )
        .pipe( buffer() )
        .pipe( gulp.dest( "public/javascripts/" ) );
} );

gulp.task( "connect", function() {
    connect.server( {
        root: "public"
    } );
} );

gulp.task( "assets", function() {
    gulp.src( [ "assets/**/*", "!assets/.gitkeep" ] )
        .pipe( gulp.dest( "public" ) );
} );

gulp.task( "jade", function() {
    gulp.src( "jade/*.jade" )
        .pipe( jade() )
        .pipe( gulp.dest( "public" ) );
} );

gulp.task( "watch", function() {
    gulp.watch( "javascript/**/*.js", [ "scripts" ] );
    gulp.watch( "jade/**/*.jade", [ "jade" ] );
    gulp.watch( "scss/**/*.scss", [ "scss" ] );
    gulp.watch( "assets/**", [ "assets" ] );
} );

gulp.task( "scss", function() {
    gulp.src( "scss/*.scss" )
        .pipe( sass().on( "error", sass.logError ) )
        .pipe( minifyCSS() )
        .pipe( gulp.dest( "./public/stylesheets" ) );
} );

gulp.task( "default", [ "scripts", "jade", "scss", "watch", "connect" ] );