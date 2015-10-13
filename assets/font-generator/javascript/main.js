var canvas = document.getElementById( "canvas" );
var context = canvas.getContext( "2d" );
var textarea = document.getElementById( "textarea" );

var names = [ "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" ];
var count = 0;

var template = "    public static final int[] {{name}} = new int[] { \n            {{content}} \n    };";

function clear() {
    canvas.width = 10;
    canvas.height = 14;
}

function next() {
    clear();

    context.fillStyle = "red";
    context.font = "13px Arial";
    context.textAlign = "center";
    context.textBaseline = "top";
    context.fillText( count.toString(), 5, 0 );

    var data = context.getImageData( 0, 0, 10, 14 ).data;
    var string = "";

    for ( var i = 0; i < data.length; i += 4 ) {
        var iteration = i / 4;

        if ( iteration % 10 !== 0 ) string += " ";

        string += data[ i + 3 ] > 80 ? "1," : "0,";

        if ( iteration % 10 === 9 ) string += " \n            ";
    }

    var output = template
        .replace( "{{name}}", names[ count ] )
        .replace( "{{content}}", string );

    textarea.value += "\n\n" + output;

    count++;

    if ( count < 10 ) requestAnimationFrame( next );
}

next();
