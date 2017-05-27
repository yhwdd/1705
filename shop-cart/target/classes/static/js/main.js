jQuery(document).ready(function(){
	var productCustomization = $('.cd-customization'),
		cart = $('.cd-cart'),
		animating = false;
	
	initCustomization(productCustomization);

	getSum();

	$('body').on('click', function(event){
		//if user clicks outside the .cd-gallery list items - remove the .hover class and close the open ul.size/ul.color list elements
		if( $(event.target).is('body') || $(event.target).is('.cd-gallery') ) {
			deactivateCustomization();
		}
	});

	function initCustomization(items) {
		items.each(function(){
			var actual = $(this),
				selectOptions = actual.find('[data-type="select"]'),
				addToCartBtn = actual.find('.add-to-cart'),
				touchSettings = actual.next('.cd-customization-trigger');

			//detect click on ul.size/ul.color list elements 
			selectOptions.on('click', function(event) { 
				var selected = $(this);
				//open/close options list
				selected.toggleClass('is-open');
				resetCustomization(selected);
				
				if($(event.target).is('li')) {
					// update selected option
					var activeItem = $(event.target),
						index = activeItem.index() + 1;
					
					activeItem.addClass('active').siblings().removeClass('active');
					selected.removeClass('selected-1 selected-2 selected-3').addClass('selected-'+index);
					// if color has been changed, update the visible product image 
					selected.hasClass('color') && updateSlider(selected, index-1);
				}
			});

			//detect click on the add-to-cart button
			addToCartBtn.on('click', function() {	
				if(!animating) {
					//animate if not already animating
					animating =  true;
					resetCustomization(addToCartBtn);

					//alert(actual.find('.add-to-cart').data("bind"))
					cartInsert(actual.find('.add-to-cart').data("bind"))

					// 商品飞向购物车
					var addcar = $(this);
					var img = document.getElementById(actual.find('.add-to-cart').data("bind")).name;
					//alert(img)
					//var img = "image/1.jpg";
					var flyer = $('<img class="u-flyer" src="'+img+'">');
					var cart = $('.cd-cart');
					var offset = cart.offset();
					/*
					var cartItems = cart.find('span'),
							text = parseInt(cartItems.text()) + 1;
					cartItems.text(text);
					 */
					flyer.fly({
						start: {
							left: event.pageX,
							top: event.pageY
						},
						end: {
							left: offset.left+10,
							top: offset.top+10,
							width: 0,
							height: 0
						},
						onEnd: function(){
							console.log(offset.left);
							//$("#msg").show().animate({width: '250px'}, 200).fadeOut(1000);
							//addcar.css("cursor","default").removeClass('orange').unbind('click');
							this.destory();
						}
					});

					//购物车+1
					addToCartBtn.addClass('is-added').find('path').eq(0).animate({
						//draw the check icon
						'stroke-dashoffset':0
					}, 300, function(){
						setTimeout(function(){
							updateCart();
							addToCartBtn.removeClass('is-added').find('em').on('webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend', function(){
								//wait for the end of the transition to reset the check icon
								addToCartBtn.find('path').eq(0).css('stroke-dashoffset', '19.79');
								animating =  false;
							});

							if( $('.no-csstransitions').length > 0 ) {
								// check if browser doesn't support css transitions
								addToCartBtn.find('path').eq(0).css('stroke-dashoffset', '19.79');
								animating =  false;
							}
						}, 600);
					});	
				}
			});

			//detect click on the settings icon - touch devices only
			touchSettings.on('click', function(event){
				event.preventDefault();
				resetCustomization(addToCartBtn);
			});
		});
	}

	function updateSlider(actual, index) {
		var slider = actual.parent('.cd-customization').prev('a').children('.cd-slider-wrapper'),
			slides = slider.children('li');

		slides.eq(index).removeClass('move-left').addClass('selected').prevAll().removeClass('selected').addClass('move-left').end().nextAll().removeClass('selected move-left');
	}

	function resetCustomization(selectOptions) {
		//close ul.clor/ul.size if they were left open and user is not interacting with them anymore
		//remove the .hover class from items if user is interacting with a different one
		selectOptions.siblings('[data-type="select"]').removeClass('is-open').end().parents('.cd-single-item').addClass('hover').parent('li').siblings('li').find('.cd-single-item').removeClass('hover').end().find('[data-type="select"]').removeClass('is-open');
	}

	function deactivateCustomization() {
		productCustomization.parent('.cd-single-item').removeClass('hover').end().find('[data-type="select"]').removeClass('is-open');
	}

	function updateCart() {
		//show counter if this is the first item added to the cart
		( !cart.hasClass('items-added') ) && cart.addClass('items-added'); 

		var cartItems = cart.find('span'),
			text = parseInt(cartItems.text()) + 1;
		cartItems.text(text);
	}

	function cartInsert(id) {
		var item =
		{
			"item_id":id,
			"num": 1
		}

		$.ajax({
			//提交数据的类型 POST GET
			type: "POST",
			//提交的网址
			url: "/my/insert",
			//提交的数据
			data: JSON.stringify(item),
			contentType: "application/json",
			//返回数据的格式
			datatype: "json",//"xml", "html", "script", "json", "jsonp", "text".
			//在请求之前调用的函数
			beforeSend: function () {

			},
			//成功返回之后调用的函数
			success: function (data) {
				//alert("插入成功!!!")
				//window.location.reload()
			},
			//调用执行后调用的函数
			complete: function (XMLHttpRequest, textStatus) {
				//HideLoading();
			},
			//调用出错执行的函数
			error: function () {
				//请求出错处理
			}
		});
	}

	function getSum() {
        $.ajax({
            //提交数据的类型 POST GET
            type: "POST",
            //提交的网址
            url: "/my/sum",
            contentType: "application/json",
            //返回数据的格式
            datatype: "json",//"xml", "html", "script", "json", "jsonp", "text".
            //在请求之前调用的函数
            beforeSend: function () {

            },
            //成功返回之后调用的函数
            success: function (data) {
            ( !cart.hasClass('items-added') ) && cart.addClass('items-added');
            var cartItems = cart.find('span'),
            			text = data ;
            		cartItems.text(text);
            },
            //调用执行后调用的函数
            complete: function (XMLHttpRequest, textStatus) {
                //HideLoading();
            },
            //调用出错执行的函数
            error: function () {
                //请求出错处理
            }
        });
    }
});