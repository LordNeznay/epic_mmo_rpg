define([
    'backbone',
    'tmpl/game',
    'underscore',
    'models/cell'
], function(
    Backbone,
    tmpl,
    Underscore,
    CellModel
){

    var View = Backbone.View.extend({
        template: tmpl,
        className: "game-view",
        fullxml: "",
        cellss: '',
        x: 9,
        y: 14,
        w: 7,
        h: 4,
        widthMap: 0,
        heightMap: 0,

        test: function(){
            var that = this;
            var cells = [];
            var map = $(fullxml).find("map");
            var width = map.attr('width');
            var height = map.attr('height');
            this.widthMap = width;
            this.heightMap = height;
            map = map.find("layer[name='Background']");
					
            var i = -1;
            var j = 0;
            $(map).find("tile").each(function(){
                ++i;
                if(i==width){
                    i = 0;
                    ++j;
                }
                if(i >= that.x-that.w && i <= that.x+that.w && j >= that.y-that.h && j <= that.y+that.h){
                    var g = $(this).attr('gid');
                    cells.push(new CellModel({gid: g, x: i, y: j}));
                }
            });
            this.cellss = cells;
            this.rend();
        },
        rend: function(){
            $.ajax({url: "utils/field_cell.html",
                context: this,
                success: function(response) {
                    this.cellTemplate = response;
                    this.showGameField();
                }
            });
        },
        initialize: function(){
            _.bindAll(this, 'move');
            $(document).bind('keypress', this.move);
            $.ajax({
                type: "GET",
                datatype: "xml",
                url: "/res/tilemap.tmx",
                success: this.parse,
                async: false
            });
            this.test();
        },
        events: {
            "click a": "hide",
        },
        render: function () {
            this.$el.html( this.template() );
            if(this.cellTemplate){
                this.showGameField();
            }
        },
        show: function () {
            this.render();
            console.log(this);
        },
        hide: function () {
            this.$el.empty();
        },
        showGameField: function(){
            var temp = [];
            this.cellss.forEach(function(element, i){
                temp.push(element);
            });
            $('.game-map').html(Underscore.template(this.cellTemplate, {cells: temp}));
        },
        parse: function(data){
            fullxml = $.parseXML(data);
        },
        move: function(ev){
            var sim = String.fromCharCode(ev.keyCode);
            var that = this;
            console.log();
            switch(sim){
                case 'w':{
                    that.y = (that.y - that.h == 0) ? that.y : that.y-1;
                    that.test();
                }; break;
                case 's':{
                    that.y = (that.y + that.h == that.heightMap-1) ? that.y : that.y+1;
                    that.test();
                }; break;
                case 'a':{
                    that.x = (that.x - that.w == 0) ? that.x : that.x-1;
                    that.test();
                }; break;
                case 'd':{
                    that.x = (that.x + that.w == that.widthMap-1) ? that.x : that.x+1
                    that.test();
                }; break;
            }
        }

    });

    return new View({el: $('.page')});
});