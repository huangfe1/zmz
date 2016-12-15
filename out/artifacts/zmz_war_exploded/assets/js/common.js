function loadDistrict(selectDom,defaultOption) {
	loadOptions("dict/district/",selectDom,defaultOption,null,["code","name"],false);
}
function loadUnit(selectDom,defaultOption){
	loadOptions("loadUnitAction.aspx",selectDom,defaultOption,null,["id","unitname"],false);
}
function loadOptions(url, selectDom, defaultValue, args, keys,addBlankOption) {
	$.ajaxSetup({async: false});
	$.getJSON(url, args, function(data) {
		$.ajaxSetup({async: true});
		selectDom.empty();
		if(addBlankOption){
			selectDom.append("<option value=''>请选择</option>");
		}
		$.each((eval(data)), function(i, ob) {
			var option = $("<option value='" + ob[keys[0]] + "'>" + ob[keys[1]]
					+ "</option>");
			if (defaultValue && defaultValue == ob[keys[0]]) {
				option.attr("selected", true);
			}
			selectDom.append(option);
		});
	});
}
$(".captchaImg").click(function reload(){
	var date=new Date();
	var s=[];
	var src=$(this).attr("src");
	var index=src.indexOf("?");
	s.push(src.substring(0, index>-1?index:src.length));
	s.push("?");
	s.push(date.getFullYear());
	s.push(date.getMonth());
	s.push(date.getDate());
	s.push(date.getHours());
	s.push(date.getMinutes());
	s.push(date.getSeconds());
	s.push(date.getMilliseconds());
	$(this).hide().attr("src",s.join("")).fadeIn();
});
function imgPrefetch(imgs){
	for(var index=0;index<imgs.length;index++){
		var img =new Image();
		img.src=imgs[index];
	}
}
function rowSelect(self){
	var check=self.find("input[type='checkbox']").get(0);
	check.checked=!check.checked;
}
function switchCss(self){
	var selectedClass="info";
	self.siblings("."+selectedClass).removeClass(selectedClass);
	self.addClass(selectedClass);
}
function initSelectValue(el,val){
	val=val||el.attr("value");
	$.each(el.children(),function(i,option){
		if(option.value==val){
			option.selected=true;
		}
	});
}
(function($){	
	$.fn.fullscreenr = function(options) {
		if(options.height === undefined) alert('Please supply the background image height, default values will now be used. These may be very inaccurate.');
		if(options.width === undefined) alert('Please supply the background image width, default values will now be used. These may be very inaccurate.');
		if(options.bgID === undefined) alert('Please supply the background image ID, default #bgimg will now be used.');
		var defaults = { width: 1400,  height: 682, bgID: 'bgimg' };
		var options = $.extend({}, defaults, options); 
		$(document).ready(function() { $(options.bgID).fullscreenrResizer(options);	});
		$(window).bind("resize", function() { $(options.bgID).fullscreenrResizer(options); });		
		return this; 		
	};	
	$.fn.fullscreenrResizer = function(options) {
		// Set bg size
		var ratio = options.height / options.width;	
		// Get browser window size
		var browserwidth = $(window).width();
		var browserheight = $(window).height();
		// Scale the image
		if ((browserheight/browserwidth) > ratio){
		    $(this).height(browserheight);
		    $(this).width(browserheight / ratio);
		} else {
		    $(this).width(browserwidth);
		    $(this).height(browserwidth * ratio);
		}
		// Center the image
		$(this).css('left', (browserwidth - $(this).width())/2);
		$(this).css('top', (browserheight - $(this).height())/2);
		return this; 		
	};
})(jQuery);
!function(w){
	if(w.utils)return;
	var Utils=function(){}
	Utils.prototype={
		Constructor :Utils,
		phoneValidate:function(e){
			var sr=/^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/g;
			return this.mobilePhone(e) || sr.test(e);
		},
		mobilePhone:function(e){
			var mr=/^((\(\d{3}\))|(\d{3}\-))?1[3-8][0-9]\d{8}$/g;
			return mr.test(e);
		}
	}
	w["utils"]= new Utils();
}(window);
if(jQuery.validator){
	jQuery.validator.addMethod("telephone", function(value, element) {
	  return this.optional(element) || window.utils.phoneValidate(value);
	},"请正确填写电话号码");
	jQuery.validator.addMethod("mobile", function(value, element) {
		  return this.optional(element) || window.utils.mobilePhone(value);
		},"请正确填写手机号码");
}
if(parent.window){
	parent.window.document.title=document.title;
}