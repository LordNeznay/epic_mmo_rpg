define([
    'backbone',
    'utils/initAnimations',
    'tmpl/game'
], function(
    Backbone,
    initAnimations,
    tmpl
){

    var canvas_map_height = 1152;
    var canvas_map_width = 1920;
    var canvas_tile_width = 128;
    var canvas_tile_height = 128;


    var View = Backbone.View.extend({
        map: '',
        pos_x: 0,
        pos_y: 0,
        entities: '',
        template: tmpl,
        canvas_background: undefined,
        canvas_background_context: undefined,
        canvas_middleground: undefined,
        canvas_middleground_context: undefined,
        canvas_middleground2: undefined,
        canvas_middleground_context2: undefined,
        canvas_frontground: undefined,
        canvas_frontground_context: undefined,
        
        all_animations: [],
        
        offsetX: 0,
        offsetY: 0,
        directMove: "none",
        fps: 30,
        sfps: 1000/500,
        dst: 0,
        minD: 0.5,
        
        forRedrawing: [],
        
        loadTilesets: 0,
        amountTilesets: 0,

        initialize: function(){
            var that = this;

            this.on("mapIsLoad", this.loadingTilesets, this);
            this.on("newPlayerPosition", this.updMap, this);
            this.on("entitiesIsLoad", this.newEntities, this);
            this.on("readyDrawMap", this.drawMap, this);
            
            that.all_animations = initAnimations;          
        },
        
        newEntities: function(newEntities){
            var that = this;
            that.entities = newEntities;
            this.updEntities();
        },
        
        updMap: function(){
            //Функция вызовится при сдвиге карты.
            this.redrawMap();
        },
        
        newPosition: function(x, y){
            var that = this;
            if((x > that.pos_x && y > that.pos_y) || (x < that.pos_x && y < that.pos_y)
                || ((x - that.pos_x) > 1) || ((x - that.pos_x) < -1) || ((y - that.pos_y) > 1) || ((y - that.pos_y) < -1)
                ){
                
            } else {
                if(x > that.pos_x && y == that.pos_y){
                    that.startMove("right");
                    that.offsetX = canvas_tile_width;
                    that.offsetY = 0;
                }
                if(x < that.pos_x && y == that.pos_y){
                    that.startMove("left");
                    that.offsetX = -canvas_tile_width;
                    that.offsetY = 0;
                }
                if(x == that.pos_x && y > that.pos_y){
                    that.startMove("down");
                    that.offsetX = 0;
                    that.offsetY = canvas_tile_width;
                }
                if(x == that.pos_x && y < that.pos_y){
                    that.startMove("up");
                    that.offsetX = 0;
                    that.offsetY = -canvas_tile_width;
                }
            }
            that.pos_x = x;
            that.pos_y = y;
        },
        
        startAnimation: function(){
            var that = this;
            this.timerId = setInterval(function(){
                that.step();
            }, 1000/that.fps);
        },
        
        stopAnimation: function(){
            clearInterval(this.timerId);
        },
        
        stopMove: function(){
            this.offsetX = 0;
            this.offsetY = 0;
            this.directMove = "none";
            this.dst = 0;
        },

        startMove: function(direct){
            this.dst = 0;
            this.directMove = direct;
        },
        
        step: function(){
            var that = this;
            var dist = canvas_tile_width*that.sfps/that.fps;
            this.dst += dist;
            switch(this.directMove){
                case "up": {
                    that.offsetY += dist;
                }; break;
                case "down": {
                    that.offsetY -= dist;
                }; break;
                case "left": {
                    that.offsetX += dist;
                }; break;
                case "right": {
                    that.offsetX -= dist;
                }; break;
            }
            
            if(Math.abs(this.offsetX) < this.minD && Math.abs(this.offsetY) < this.minD){
                this.stopMove();
            }
            
            if(this.dst > canvas_tile_width){
                this.stopMove();
            }
            this.redrawMap();
            this.stepEntities();
        },
        
        setMap: function(map){
            this.map = map;
        },
        
        drawTile: function(gid, x, y){
            var that = this;
            
            that.map.tilesets.forEach(function(tileset){
                if(tileset.firstgid <= gid && gid < tileset.firstgid + (tileset.imageheight*tileset.imagewidth/tileset.tileheight/tileset.tilewidth)){
                    var dgid = gid - tileset.firstgid;
                    var xx = 0;
                    var yy = 0;
                    xx = dgid * tileset.tilewidth;
                    while(xx >= tileset.imagewidth){
                        yy += tileset.tileheight;
                        xx -= tileset.imagewidth;
                    }
                    that.canvas_background_context.drawImage(tileset.image, xx, yy, tileset.tilewidth, tileset.tileheight, x + that.offsetX, y + that.offsetY, tileset.tilewidth, tileset.tileheight);
                }
            });
        },
        
        loadingTilesets: function(){
            var dop_tileset = {
                firstgid: -1,
                image: "0.png",
                imageheight: canvas_tile_height, 
                imagewidth: canvas_tile_width, 
                name: "zeroTile",
                tileheight: canvas_tile_height, 
                tilewidth: canvas_tile_width
            }
            this.map.tilesets.push(dop_tileset);
            
            var that = this;
            var amount = 0;
            that.loadTilesets = 0;
            //console.log(this.map);
            this.map.tilesets.forEach(function(tileset){
                amount += 1;
                var pic = new Image();
                pic.src    = 'http://' + document.location.host + '/res/' + tileset.image;
                pic.onload = function() { 
                    tileset.image = pic;
                    that.loadTilesets++;
                    that.trigger("readyDrawMap");
                }
            });
            this.amountTilesets = amount;
        },





        
        //anim, entity
        checkNeedOffset: function(a, e){
            var that = this;
            if((a.x > e.x && a.y > e.y) || (a.x < e.x && a.y < e.y)
                || ((a.x - e.x) > 1) || ((a.x - e.x) < -1) || ((a.y - e.y) > 1) || ((a.y - e.y) < -1)
                ){
            } else {
                if(e.x > a.x && e.y == a.y){
                    a.direct = "right";
                    a.offsetX = -canvas_tile_width;
                    a.offsetY = 0;
                    a.dst = 0;
                    a.anim = 'move';
                    a.isRotate = false;
                }
                if(e.x < a.x && e.y == a.y){
                    a.direct = "left";
                    a.offsetX = canvas_tile_width;
                    a.offsetY = 0;
                    a.dst = 0;
                    a.anim = 'move';
                    a.isRotate = true;
                }
                if(e.x == a.x && e.y > a.y){
                    a.direct = "down";
                    a.offsetX = 0;
                    a.offsetY = -canvas_tile_width;
                    a.dst = 0;
                    a.anim = 'move';
                }
                if(e.x == a.x && e.y < a.y){
                    a.direct = "up";
                    a.offsetX = 0;
                    a.offsetY = canvas_tile_width;
                    a.dst = 0;
                    a.anim = 'move';
                }
                
            }
            a.x = e.x;
            a.y = e.y;
        },
        
        stepEntities: function(){
            var that = this;
            var dist = canvas_tile_width*that.sfps/that.fps;
            that.forRedrawing.forEach(function(anim){
                anim.dst += dist;
                switch(anim.direct){
                    case "up": {
                        anim.offsetY -= dist;
                    }; break;
                    case "down": {
                        anim.offsetY += dist;
                    }; break;
                    case "left": {
                        anim.offsetX -= dist;
                    }; break;
                    case "right": {
                        anim.offsetX += dist;
                    }; break;
                }
                
                if((Math.abs(anim.offsetX) < this.minD && Math.abs(anim.offsetY) < this.minD)
                    || anim.dst > canvas_tile_width
                  ){
                    anim.offsetX = 0;
                    anim.offsetY = 0;
                    anim.direct = "none";
                    anim.dst = 0;
                    anim.anim = 'wait';
                }
            });
            this.drawEntities();
        },
        
        updEntities: function(){
            var that = this;
            var newAnim = [];
            //Обходим все новые сущности
            this.entities.entities.forEach(function(entity){
                //Ищем существующее изображение сущности
                var anim = undefined;
                that.forRedrawing.forEach(function(_anim){
                    if(_anim.number == entity.number){
                        anim = _anim;
                        that.checkNeedOffset(anim, entity);
                        return;
                    }
                });
                //Создаем его, если не находим
                if(anim == undefined){
                    anim = {
                        x: entity.x,
                        y: entity.y,
                        offsetX: 0,
                        offsetY: 0,
                        direct: 'none',
                        dst: 0,
                        anim: 'move',
                        number: entity.number,
                        command: entity.image,
                        isRotate: false
                    }        
                }
                newAnim.push(anim);
                
            });
            
            that.forRedrawing = newAnim;
        },
        
        drawEntities: function(){
            this.canvas_middleground_context.clearRect(0, 0, this.canvas_middleground.width, this.canvas_middleground.height);

            var that = this;
            that.forRedrawing.forEach(function(anim){
                var pic = undefined;
                var steps;
                switch(anim.command){
                    case "red_people.png":{
                        pic = that.all_animations['red_player'][anim.anim].el;
                        steps = that.all_animations['red_player'][anim.anim].steps;
                    }; break;
                    case "blue_people.png":{
                        pic = that.all_animations['blue_player'][anim.anim].el;
                        steps = that.all_animations['blue_player'][anim.anim].steps;
                    }; break;
                    case "flag.png":{
                        pic = that.all_animations['flag'][anim.anim].el;
                        steps = that.all_animations['flag'][anim.anim].steps;
                    }; break;
                    default: {
                        console.log(anim.command);
                    }; break;
                }

                if(!anim.isRotate){
                    that.canvas_middleground_context.drawImage(pic, 
                        canvas_tile_width * Math.floor(anim.dst / (canvas_tile_width / steps)),
                        0,
                        canvas_tile_width,
                        canvas_tile_height,
                        (anim.x - that.pos_x + 7) * canvas_tile_width  + that.offsetX + anim.offsetX,
                        (anim.y - that.pos_y + 4) * canvas_tile_height  + that.offsetY + anim.offsetY,
                        canvas_tile_width,
                        canvas_tile_height);
                } else {
                    that.canvas_middleground_context.save();
                    that.canvas_middleground_context.scale(-1, 1);
                    that.canvas_middleground_context.drawImage(pic, 
                        canvas_tile_width * Math.floor(anim.dst / (canvas_tile_width / steps)),
                        0,
                        canvas_tile_width,
                        canvas_tile_height,
                        -((anim.x - that.pos_x + 7 + 1) * canvas_tile_width  + that.offsetX + anim.offsetX),
                        (anim.y - that.pos_y + 4) * canvas_tile_height  + that.offsetY + anim.offsetY,
                        canvas_tile_width,
                        canvas_tile_height);
                    that.canvas_middleground_context.restore();
                }
            });
            
            this.canvas_middleground_context2.clearRect(0, 0, this.canvas_middleground.width, this.canvas_middleground.height);
            this.canvas_middleground_context2.drawImage(this.canvas_middleground, 0, 0);
        },
        
        drawMap: function(){
            //Если картинки не догружены, то выйти
            if(this.amountTilesets != this.loadTilesets) return;
            
            var that = this;
            that.map.layers.forEach(function(layer){
                if(!(layer.name == "Background" || layer.name == "Frontground")){
                    return;
                }
                var dx = that.map.tilewidth;
                var dy = that.map.tileheight;
                var x = -dx;
                var y = -dy;
                layer.data.forEach(function(gid){
                    that.drawTile(gid, x  + that.offsetX, y  + that.offsetY);
                    x += dx;
                    while(x >= (that.map.width-1) * dx){
                        x = -dx;
                        y += dy;
                    }
                });
            });
        },
        
        getTileGID: function(x, y, layer){
            if(x >= this.map.width || x < 0) return -1;
            if(y >= this.map.height || y < 0) return -1;
            return layer.data[y * this.map.width + x];
        },
        
        redrawMap: function(){
            //Отменить движение
            //this.stopMove();
            //Если картинки не догружены, то выйти
            if(this.amountTilesets != this.loadTilesets) return;
            var that = this;
            
            that.map.layers.forEach(function(layer){
                if(!(layer.name == "Background" || layer.name == "Frontground")){
                    return;
                }
                var dx = that.map.tilewidth;
                var dy = that.map.tileheight;
                
                for(var j = that.pos_y - 5; j <= that.pos_y + 5; ++j){
                    for(var i = that.pos_x - 8; i <= that.pos_x + 8; ++i){
                        that.drawTile(that.getTileGID(i, j, layer), (i - that.pos_x + 7)*dx, (j - that.pos_y + 4)*dy);
                    }
                }
            });
            //alert("1");
        }
        
    });

    return View;
});