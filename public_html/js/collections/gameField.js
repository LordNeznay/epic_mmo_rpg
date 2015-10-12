define([
    'backbone',
	'models/cell'
], function(
    Backbone,
	CellModel
){
	
	function parse(data, x, y, w, h){
		var map = $(data).find("map");
		var width = map.attr('width');
		var height = map.attr('height');
				
		var i = 0;
		var j = 0;
				
		$(data).find("tile").each(function(){
			++i;
			if(i==width){
				i = 0;
				++j;
			}
			
			if(i >= x-w && i <= x+w && j >= y-h && j <= y+h){
				var g = $(this).attr('gid');
				cells.push(new CellModel({gid: g, x: i, y: j}));
			}
		});
	}

	function parse2(data){
		parse($.parseXML(data), 9, 14, 4, 4);
	}
	
	cells = [];
	$.ajax({
        type: "GET",
		datatype: "xml",
        url: "/utils/tilemap.tmx",
        success: parse2,
		async: false
    });

	var Collection = Backbone.Collection.extend({
		model: CellModel,
		initialize: function(){

		}
	});

    return new Collection(cells);
});