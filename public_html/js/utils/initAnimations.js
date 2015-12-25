define([

], function(

){
    var animations = [];
    
    animations['red_player'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/wait_red.png',
            'steps' : 5,
            'onend' : null
        },
        'move': {
            'el'    : null,
            'src'   : 'res/move_red.png',
            'steps' : 8,
            'onend' : null
        }
    };
    animations['blue_player'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/wait_blue.png',
            'steps' : 5,
            'onend' : null
        },
        'move': {
            'el'    : null,
            'src'   : 'res/move_blue.png',
            'steps' : 8,
            'onend' : null
        }
    };   
    animations['flag'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/flag.png',
            'steps' : 1,
            'onend' : null
        },
        'move': {
            'el'    : null,
            'src'   : 'res/flag.png',
            'steps' : 1,
            'onend' : null
        }
    };   
    
    for(var t in animations){
        for(var a in animations[t]){
            var img = new Image();
            img.src = animations[t][a].src;
            animations[t][a].el = img;
        }
    }
    
    return animations;
});