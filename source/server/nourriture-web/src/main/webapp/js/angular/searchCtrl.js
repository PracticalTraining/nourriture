//app.controller("search", function($scope, $http) {
//	$scope.search = function() {
//		var keyword = {
//				foodname:$scope.submit
//		}
//		if ($scope.submit != "null") {
//			$http({
//				method : 'GET',
//				url : "ws/food/food/search",
//				params : keyword
//			}).success(function(data) {
//				if (!data.errorCode) {
//					$cookieStore.put("username", $scope.username);
//				} else {
//					$scope.hintShow = true;
//					$scope.hint = "what you search is not here!";
//				}
//			});
//		}
//	}
//});

app.controller("search", function($scope, $http) {
	$scope.search = function() {
		var keyword = {
			name : $scope.foodname
		}
		console.log(keyword);
		if ($scope.submit != "null") {
			$http({
				method : 'GET',
				url : "ws/food/searchByName",
				params : keyword
			}).success(function(data) {
				console.log(data);
				if (!data.errorCode) {
					$scope.hint = data;
					/*
					 * 添加你的代码 首先解析json 然后将原来的内容清空 将新的内容插进去
					 */
					var foods = data.foods;
					if (foods.length > 0) {
						for (i = 0; i < foods.length; i++) {
							console.log(foods[i].name)
							
							var foodcol3='<div class="col-md-3">'
								+'<div class="grid1">'
								+'<a href="fooddetail.html">'
								+'<div class="view view-first">'
								+'<div class="index_img">'
								+'<img src='+foods[i].picture+' class="img-responsive" alt="">'
								+'</div>'
								+'<div class="sale">RMB '+foods[i].price+'</div>'
								+'<div class="mask">'
								+'<div class="info">'
								+'<i class="search"> </i> Show More'
								+'</div>'
								+'<ul class="mask_img">'
								+'<li class="star"><img src="./images/star.png" alt=""></li>'
								+'<li class="set"><img src="./images/set.png" alt=""></li>'
								+'<div class="clearfix"></div>'
								+'</ul>'
								+'</div>'
								+'</div>'
								+'</a>'
								+'<i class="home"></i>'
								+'<div class="inner_wrap">'
								+'<h3>'+foods[i].name+'</h3>'
								+'<ul class="star1">'
								+'<li><a href="#"> <img src="./images/star1.png" alt="">(236)</a></li>'
								+'</ul>'
								+'</div>'
								+'</div>'
								+'</div>';
	
							$(".top_grid1").append(foodcol3);
							
						}
					}

				} else {
					$scope.hintShow = "Something wrong happened!";
					$scope.hint = data;
				}
			});
		}
	}
});