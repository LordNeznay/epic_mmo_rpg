define([

], function(

){
    var animations = [];
    
    animations['red_player'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/wait_red.png',
            'steps' : 5,
            'width' : 128
        },
        'move': {
            'el'    : null,
            'src'   : 'res/move_red.png',
            'steps' : 8,
            'width' : 128
        },
        'attack_right': {
            'el'    : null,
            'src'   : 'res/attack_right_red.png',
            'steps' : 7,
            'width' : 128
        },
        'attack_bottom': {
            'el'    : null,
            'src'   : 'res/attack_bottom_red.png',
            'steps' : 6,
            'width' : 128
        },
        'attack_top': {
            'el'    : null,
            'src'   : 'res/attack_top_red.png',
            'steps' : 6,
            'width' : 128
        }
    };
    animations['blue_player'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/wait_blue.png',
            'steps' : 5,
            'width' : 128
        },
        'move': {
            'el'    : null,
            'src'   : 'res/move_blue.png',
            'steps' : 8,
            'width' : 128
        },
        'attack_right': {
            'el'    : null,
            'src'   : 'res/attack_right_blue.png',
            'steps' : 7,
            'width' : 128
        },
        'attack_bottom': {
            'el'    : null,
            'src'   : 'res/attack_bottom_blue.png',
            'steps' : 6,
            'width' : 128
        },
        'attack_top': {
            'el'    : null,
            'src'   : 'res/attack_top_blue.png',
            'steps' : 6,
            'width' : 128
        }
    };   
    animations['flag'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/flag.png',
            'steps' : 1,
            'width' : 128
        },
        'move': {
            'el'    : null,
            'src'   : 'res/flag.png',
            'steps' : 1,
            'width' : 128
        }
    };  
    animations['blue_flag'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/blue_flag.png',
            'steps' : 1,
            'width' : 128
        },
        'move': {
            'el'    : null,
            'src'   : 'res/blue_flag.png',
            'steps' : 1,
            'width' : 128
        }
    };  
    animations['red_flag'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/red_flag.png',
            'steps' : 1,
            'width' : 128
        },
        'move': {
            'el'    : null,
            'src'   : 'res/red_flag.png',
            'steps' : 1,
            'width' : 128
        }
    }; 
    animations['target'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/target.png',
            'steps' : 1,
            'width' : 128
        },
        'move': {
            'el'    : null,
            'src'   : 'res/target.png',
            'steps' : 1,
            'width' : 128
        }
    };  
    animations['ifError'] = {
        'wait': {
            'el'    : null,
            'src'   : 'res/0.png',
            'steps' : 1,
            'width' : 128
        },
        'move': {
            'el'    : null,
            'src'   : 'res/0.png',
            'steps' : 1,
            'width' : 128
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