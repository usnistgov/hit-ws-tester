(function() {

	var app = angular.module("validator", ['720kb.tooltips']);
	app.config(function ($httpProvider,$compileProvider) {
		$compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|blob):/);
	    $httpProvider.defaults.transformRequest = function(data){
	        if (data === undefined) {
	            return data;
	        }
	        return $.param(data);
	    };
	    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
	});
	
	app.factory("toolkit",['$http','urls','model','config','ConfigObject','general','alerts',function($http,urls,model,config,co,gen,alerts){
		var instance;
		
		instance = {
			get : function(){
				model.domains = [];
				model.profiles = {};
				model.tables = {};
				gen.loading = true;
				var promise = $http.get(urls.resources)
				.success(function(data, status, headers, config) {
					var resources = data.domains;
					for (var key in resources) {
						model.domains.push(resources[key].name);
						model.profiles[resources[key].name] = resources[key].profiles;
						model.tables[resources[key].name] = resources[key].tables;
					}
					gen.loading = false;
					
				})
				.error(function(data, status, headers, config) {
					alerts.errors.push("Could not reach the server, error status :"+status);
					
				});
				return promise;	
			},
			createConfig : function(context){
				tablesOID  = "";
				for(var key in config.selectedTab){
					tablesOID = tablesOID + config.selectedTab[key].OID +":";
				}
				console.log(config.selectedPro);
				contextB = context === "Context Based";
				return {
					profile : config.selectedPro.OID,
					domain  : config.selecteDom,
					tables	: tablesOID,
					context : contextB
				};
			},
			validate : function(){
				gen.loading = true;
				var promise = $http.post(urls.validation,{
					profile : co.params.profile,
					domain  : co.params.domain,
					tables	: co.params.tables,
					contextB : co.params.context,
					message : co.message
				})
				.success(function(data, status, headers, config) {
					gen.loading = false;
					return data;
				})
				.error(function(data,status,headers,config){
					if(status === 404)
						alerts.errors.push("Could not reach the server");
					else if(status === 500)
					{
						gen.loading = false;
						gen.tab = 1;
						gen.active = 3;
						alerts.errors.push("Could not reach the web service, please check the Soap Endpoint");
					}
				});
				return promise;	
			},
			batch : function(files,i){
				console.log("In Batch "+i+"/"+files.length);
				if(files.length > i){
					
					var fd = new FormData();
					fd.append("file",files[i]);
					$http.post("batch", fd, {
			            transformRequest: angular.identity,
			            headers: {'Content-Type': undefined}
					})
					.success(function(data){
						if(i != (files.length - 1))
						gen.loading_msg = "Processing message "+(i+2)+"/"+files.length;
						instance.batch(files,i+1);
					})
					.error(function(data){
						gen.loading_msg = "Error";
					});
				}
				else {
					$http.post("batch",{
						message : "finish"
					})
					.success(function(data) {
						if(data.status){
							console.log("Batch successful");
							gen.loading = false;
							gen.loading_msg = "Complete";
							gen.response.type = data.type;
							gen.zip_file = data.path;
							gen.file_size = data.size;
							gen.time = data.time;
							gen.nbR = data.nbR;
							gen.tab = gen.tab + 1;
						}
					});	
				}
			}
		};
		
		return instance;
	}]);
	
	app.directive('compile', ['$compile', function ($compile) {
		  return function(scope, element, attrs) {
		    scope.$watch(
		      function(scope) {
		        return scope.$eval(attrs.compile);
		      },
		      function(value) {
		        element.html(value);
		        $compile(element.contents())(scope);
		      }
		   )};
	}]);
	
	app.value("model",{
		domains : [],
		profiles : {},
		tables : {}
	});
	
	app.value("config",{
		selecteDom : "",
		selectedPro : "",
		selectedTab : []
	});
	
	app.value("ConfigObject",{
		params : {},
		message : ""
	});
	
	app.value("general",{
		tab : 1,
		response : {
			type : "",
			content : "",
			xml : ""
		},
		zip_file : "",
		accept : false,
		step : 1,
		loading : false,
		loading_msg : "Processing...",
		file_size : "",
		nbR : 0,
		active : 1,
		time : 0
	});
	
	app.value("urls",{
		resources : "resources",
		validation : "validate",
	});
	
	app.value("custom",{
		resources : "resources",
		validation : "validate",
	});
	
	app.directive("profileSelection", ['$http','toolkit','model','config','ConfigObject','general',function($http,toolkit,model,config,co,gen) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/profiles.html",
			controller: function() {
				this.preloaded = true;
				this.fn = "Choose an XML File";
				this.fileError = false;
				this.selectAll = true;
				this.btnText = "Select All";
				this.context = "";
				this.contextF = "";
				this.file = false;
				this.needed = true;
				var ctrl = this;
				
				this.setPreloaded = function(b){
					preloaded = b;
				};
				
				this.step = function(){
					return gen.step;
				};
				
				this.doms = function(){
					return model.domains;
				};
				
				this.pros = function(){
					return model.profiles[config.selecteDom];
				};
				
				this.tabs = function(){
					return model.tables[config.selecteDom];
				};
				
				this.tabisSelected = function(tab){
					return config.selectedTab.indexOf(tab) > -1;
				};
				
				this.tabPosition = function(tab){
					return config.selectedTab.indexOf(tab) + 1;
				};
				
				this.selecT = function(tab){
					var index = config.selectedTab.indexOf(tab);
					if(index == -1)
						config.selectedTab.push(tab);
					else
						config.selectedTab.splice(index, 1);
				};
				
				this.canGo = function(){
					return config.selecteDom != "" && config.selectedPro != "" && (!this.needed ||  (config.selectedTab != [] && this.context != "" && (this.context === "Context Free" || this.file && this.context === "Context Based")));
				};
				
				this.toggleSelect = function(){
					tab = this.tabs();
					if(this.selectAll)
					{
						for(var key in tab){
							if(!this.tabisSelected(tab[key]))
								this.selecT(tab[key]);
						}
						this.selectAll = false;
						this.btnText = "Unselect All";
					}
					else 
					{
						config.selectedTab = [];
						this.selectAll = true;
						this.btnText = "Select All";
					}
				};
				
				this.selectD = function(dom){
					config.selecteDom = dom;
					gen.step = 2;
				};
				
				this.selectP = function(pro){
					config.selectedPro = pro;
					this.needed = (pro.params === "needed");
					if(this.needed && this.selectAll)
						this.toggleSelect();
					gen.step = 3;
				};
				
				this.domisSelected = function(){
					return config.selecteDom != "";
				};
				
				this.proisSelected = function(){
					return config.selectedPro != "";
				};
				
				this.selectedPro = function(){
					return config.selectedPro.name;
				};
				
				this.selecteDom = function(){
					return config.selecteDom;
				};
				
				this.back = function(step){
					if(!this.selectAll)
						this.toggleSelect();
						
					if(step === 2)
					{
						config.selecteDom = "";
						config.selectedPro = "";
						config.selectedTab = [];
						gen.step = 1;
					}
					else if(step === 3)
					{
						config.selectedPro = "";
						config.selectedTab = [];
						gen.step = 2;
					}
					
				}
				
				toolkit.get().then(function(data){
					console.log(model.domains);
					console.log(model.profiles);
				});
				
				this.next = function(){
					co.params = toolkit.createConfig(this.context);
					console.log(co);
					gen.tab = gen.tab + 1;
					if(ctrl.isCB()){
						ctrl.fn = "Choose an XML File";
						ctrl.file = false;
					}
				}
				
				this.isCB = function(){
					return this.context === "Context Based";
				}
				
				this.isError = function(){
					return true && this.fileError;
				}
			},
			link : function( $scope, element, attributes, ctrl ) {
				
				$scope.upContext = function(files) {
					if(files[0].type === "text/xml"){
						ctrl.fn = files[0].name;
						ctrl.fileError = false;
						var fd = new FormData();
				        fd.append('file', files[0]);
				        $http.post("up", fd, {
				            transformRequest: angular.identity,
				            headers: {'Content-Type': undefined}
				        })
				        .success(function(){
				        	ctrl.file = true;
				        })
				        .error(function(){
				        	ctrl.file = false;
				        });
					}
					else {
						ctrl.fileError = true;
					}
				};
			},
			controllerAs: "profile"
		};
	}]);
	
	app.directive("messageArea", ['toolkit','model','config','ConfigObject','general','$http',function(toolkit,model,config,co,general,$http) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/message.html",
			controller: function() {
				this.content = "";
				this.response = "";
				this.files = null;
				this.isBatch = false;
				this.filename = "";
				this.notZIP = false;
				var ctrl = this;
				
				this.validate = function(){
					if(!this.isBatch){
						co.message = this.content;
						toolkit.validate().then(function(data){
							general.response.type = data.data.type;
							if(general.response.type === 'new')
								general.response.content = JSON.parse(data.data.content);
							else
								general.response.content = data.data.content;
							
							general.time = data.data.time;
							var blob = new Blob([data.data.xml],{type: 'text/xml'});
							general.response.xml = URL.createObjectURL(blob);
							console.log("XML : "+general.response.xml);
							general.tab = general.tab + 1;
							ctrl.content = "";
							ctrl.response = "";
							ctrl.files = null;
							ctrl.isBatch = false;
							ctrl.filename = "";
							ctrl.notZIP = false;
						});
					}
					else {
						
							general.loading = true;
							general.loading_msg = "Processing 1/"+ctrl.files.length;
						
						$http.post("batch",{
							profile : co.params.profile,
							domain  : co.params.domain,
							tables	: co.params.tables,
							contextB : co.params.context,
							message : "init"
						})
						.success(function(data) {
							if(data.status){
								console.log("Init successful");
								
								toolkit.batch(ctrl.files,0);
								ctrl.content = "";
								ctrl.response = "";
								ctrl.files = null;
								ctrl.isBatch = false;
								ctrl.filename = "";
								ctrl.notZIP = false;
							}
						});	
					}
				};
				
				this.showVal = function(){
					return (this.content != "" && !this.isBatch) || (this.files != null && this.isBatch);
				};
				
				this.back = function(){
					general.tab = general.tab - 1;
				};
				
				
			},
			link : function( $scope, element, attributes, ctrl ) {
				
				$scope.selectedBatch = function(files) {
					console.log("NB : "+files.length);
					$scope.$apply(function() {
						ctrl.files = files;
						ctrl.filename = files.length + " files selected";
					});
				};
			},
			controllerAs: "msg"
		};
	}]);
	
	app.directive("uploadArea", ['toolkit','model','config','ConfigObject','general','$http','alerts',function(toolkit,model,config,co,general,$http,alerts) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/upload.html",
			controller: function() {
				this.file = null;
				this.type = "";
				this.name = "";
				this.filename = "";
				this.notXML = false;
				var ctrl = this;
				
				this.showUp = function(){
					return this.name != "" && this.type != "" && this.file != null;
				};
				
				this.up = function(){
					var fd = new FormData();
					fd.append("file",this.file);
					fd.append("type",this.type);
					fd.append("name",this.name);
					
					 $http.post("load", fd, {
				            transformRequest: angular.identity,
				            headers: {'Content-Type': undefined}
				     })
				     .success(function(data){
				    	if(data.status){
				    		alerts.success.push(ctrl.name+" "+ctrl.type+" has been successfuly uploaded using OID : "+data.oid);
				    	}
				    	else {
				    		alerts.errors.push(ctrl.name+" "+ctrl.type+" upload has failed : "+data.errorDescription);
				    	}
				    	ctrl.file = null;
				    	ctrl.type = "";
				    	ctrl.name = "";
				    	ctrl.filename = "";
				    	ctrl.notXML = false;
				     })
				     .error(function(data){
				    	 alerts.success.push("Unable to reach the server");
				    	 console.log(data); 
				    	 ctrl.file = null;
				    	 ctrl.type = "";
				    	 ctrl.name = "";
				    	 ctrl.filename = "";
				    	 ctrl.notXML = false;
				     });
				};
				
			},
			link : function( $scope, element, attributes, ctrl ) {
				
				$scope.selected = function(files) {
					if(files[0].type === "text/xml"){
						console.log("in selected xml");
						$scope.$apply(function() {
							ctrl.file = files[0];
							ctrl.filename = files[0].name;
							ctrl.notXML = false;
						});
					}
					else{
						console.log("in selected not xml");
						$scope.$apply(function() {
							ctrl.file = null;
							ctrl.filename = "";
							ctrl.notXML = true;
						});
					}
				};
			},
			controllerAs: "upload"
		};
	}]);
	
	app.value("uploads",{
		profile : { 
			fn : "Choose an XML File",
			oid : "",
			status : "w"
		},
		tables : [],
	});
	
	app.filter("sanitize", ['$sce', function($sce) {
		  return function(htmlCode){
		    return $sce.trustAsHtml(htmlCode);
		  }
	}]);
	
	app.directive("responseArea", ['toolkit','model','config','ConfigObject','general',function(toolkit,model,config,co,general) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/response.html",
			controller: function() {
				this.getResponse = function(){
					return general.response.content;
				};
				
				this.isBatch = function(){
					return general.response === "ZIP";
				};
				
				this.is = function(a){
					return general.response.type === a;
				};
				
				this.newV = function(){
					general.tab = 1;
				};
				
				this.newV = function(){
					general.tab = 1;
				};
				
				this.getPath = function(){
					return general.zip_file;
				};
				
				this.fSize = function(){
					return general.file_size;
				};
				
				this.nbR = function(){
					return general.nbR;
				};
				
				this.time = function(){
					return general.time;
				}
			},
			link : function( $scope, element, attributes, ctrl ) {
				$scope.$watch(function () {
				    return general.response;
				},
				function(newVal, oldVal) {
			        angular.element( '#result table' ).toggleClass( 'table table-striped table-condensed table-bordered' );
				}, true);
				
				
			},
			controllerAs: "resp"
		};
	}]);
	
	app.directive("alertsArea", ['alerts','general',function(alerts,gen) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/alerts.html",
			controller: function() {
				this.errors = function(){
					return alerts.errors;
				};
				
				this.success = function(){
					return alerts.success;
				};
				
				this.dismiss = function(id,type){
					if(type === 's'){
						alerts.success.splice(id,1);
					}
					else if(type === 'e'){
						alerts.errors.splice(id,1);
					}
				};
				
				
			},
			link : function( $scope, element, attributes, ctrl ) {
				
			},
			controllerAs: "alerts"
		};
	}]);
	
	app.directive("newReport", ['alerts','general',function(alerts,gen) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/new_report.html",
			controller: function() {
				this.getReport = function(){
					return gen.response.content;
				};
				
				this.getD = function(){
					return "DDDD";
				};
				
				this.hasMeta = function(meta){
					if(meta === undefined)
						return false;
					if(meta === null)
						return false;
					if(Object.getOwnPropertyNames(meta).length === 0)
						return false;
					return true;
				};
				
				this.hasStack = function(stack){
					if(stack === undefined)
						return false;
					if(stack === null)
						return false;
					if(stack === [])
						return false;
					return true;
				};
			},
			controllerAs: "r"
		};
	}]);
	
	app.directive("endPoint", ['alerts','$http',function(alerts,$http) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/endp.html",
			controller: function() {
				var ctrl = this;
				this.useDefault = function(){
					this.url = "http://hl7v2.ws.nist.gov/hl7v2ws//services/soap/MessageValidationV2";
					this.change();
				};
				
				this.change = function(){
					$http.post("endp",{
						url : ctrl.url
					})
					.success(function(data, status, headers, config) {
						alerts.success.push("Soap Endpoint Changed");
					})
					.error(function(data,status,headers,config){
						alerts.errors.push("Could not change Soap Endpoint");
					});
				}

			},
			link : function( $scope, element, attributes, ctrl ) {
				
			},
			controllerAs: "ep"
		};
	}]);
	
	app.value("alerts",{
		errors : [],
		success : []
	});
	
	app.controller('GeneralController', ['toolkit','general', function(toolkit,general) {
		this.activeT = function(a){
			return general.active === a;
		};
		
		this.activate = function(a){
			general.active = a;
		};
		
		this.isSelected = function(a){
			return general.tab === a;
		};
		
		this.isLoading = function(){
			return general.loading;
		};
		
		this.loadMsg = function(){
			return general.loading_msg;
		};
		
		this.getXML = function(){
			return general.response.xml;
		};
		
		this.isBatch = function(){
			return general.response === "ZIP";
		};
		
	}]);
	
	app.controller('ProfileController', ['toolkit','general','config', function(toolkit,gen,config) {
		this.reload = function(){
			config.selecteDom = "";
			config.selectedPro = "";
			config.selectedTab = [];
			gen.step = 1;
			toolkit.get().then(function(data){
			});
		}
	}]);
	
})();
