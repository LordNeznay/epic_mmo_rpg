define([

], function(

){
    var animations = [];
    
    animations['red_player'] = {
        'move': {
            'el'    : null,
            'src'   : 'res/move_red.png',
            'steps' : 8,
            'onend' : null
        }
    };
    animations['blue_player'] = {
        'move': {
            'el'    : null,
            'src'   : 'res/move_blue.png',
            'steps' : 8,
            'onend' : null
        }
    };   

    /*animations.forEach(function(classAnim){

        classAnim.forEach(function(anim){
            var img = new Image();
            img.src = anim.src;
            console.log(anim);
            anim.el = img;
        });
    });*/
    
    for(var t in animations){
        for(var a in animations[t]){
            var img = new Image();
            img.src = animations[t][a].src;
            animations[t][a].el = img;
        }
    }
    
    return animations;
});