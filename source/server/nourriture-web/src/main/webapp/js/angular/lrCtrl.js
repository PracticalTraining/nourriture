app.controller("lr", function($cookieStore,$scope,$http) {
	$scope.name = "Login";
	$scope.identity = "customer";
	$scope.ridentity = "customer";
	if($cookieStore.get("username")){
		$scope.name = $cookieStore.get("username");
    	$scope.loginBox = false;
	} else {
		$scope.loginBox = true;
	}
	
	$scope.login = function(){
		$scope.hintShow = false;
    	$scope.hint = null;
		var user = {
			name:$scope.username,
			password:$scope.password
		};
		if($scope.identity == "customer")
		{
			$http({method:'GET',url:"ws/customer/login",params:user}).success(function(data){
		        if(!data.errorCode){
		        	$scope.name = $scope.username;
		        	$scope.loginBox = false;
		        	$cookieStore.put("username",$scope.username);
		        } else {
		        	$scope.hintShow = true;
		        	$scope.hint = "user name or password is not correct!";
		        }
		    });
		} else {
			$http({method:'GET',url:"ws/manuFacturer/login",params:user}).success(function(data){
				if(!data.errorCode){
					$scope.name = $scope.username;
					$scope.loginBox = false;
					$cookieStore.put("username",$scope.username);
		        } else {
		        	$scope.hintShow = true;
		        	$scope.hint = "user name or password is not correct!";
		        }
		    });
		}
	}
	
	$scope.logout = function(){
		$scope.name = "Login";
    	$scope.loginBox = true;
    	$cookieStore.remove("username");
	}
	
	$scope.register = function(){
		if($scope.identity == "customer")
		{
			var customer = {
					name:$scope.rusername,
					password:$scope.rpassword,
					sex:0,
					age:0
			}
			$http.post("ws/customer",customer).success(function(data){
				console.log(data);
		        if(!data.errorCode){
		        	$scope.name = $scope.rusername;
		        	$scope.loginBox = false;
		        	$cookieStore.put("username",$scope.rusername);
		        } else {
		        	$scope.rhintShow = true;
		        	$scope.rhint = "register information is not correct!";
		        }
		    });
		} else {
			var manu = {
					name:$scope.rusername,
					password:$scope.rpassword,
					companyName:"no name",
					description:"no description"
			}
			$http.post("ws/manuFacturer",manu).success(function(data){
		        if(!data.errorCode){
		        	$scope.name = $scope.rusername;
		        	$scope.loginBox = false;
		        	$cookieStore.put("username",$scope.rusername);
		        } else {
			        $scope.rhintShow = true;
			        $scope.rhint = "register information is not correct!";
			    }
		    });
		}
	}
});