(function() {

	var app = angular.module("validator", ['720kb.tooltips','ui.bootstrap','ngAnimate']);
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
					message : co.message,
					dqa		: co.params.dqa,
					dqaFilters : co.params.dqaFilters
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
	
	app.value("DQAFilters",{
		filters : []
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
	
	app.controller('DQAController', ['DQAFilters','general','config', '$uibModalInstance',function(dqaF,gen,config,$uibModalInstance) {
		this.title = "DQA Options";
		this.search = {
				code : '',
				desc : ''
		};
		
		this.nbFilters = function(){
        	return dqaF.filters.length;
        };
        
        this.ok = function(){
        	$uibModalInstance.close();
        };
        
        this.toggle = function(is){
			var index = dqaF.filters.indexOf(is.code);
			if(index == -1)
				dqaF.filters.push(is.code);
			else
				dqaF.filters.splice(index, 1);
        };
        
		this.isSelected = function(is){
			for(var key in dqaF.filters){
				if(dqaF.filters[key] === is.code)
					return true;
			}
			return false;
		};
        
		this.selectAll = function(){
			for(var key in this.dqaOptions.errors){
				if(!this.isSelected(this.dqaOptions.errors[key]))
					this.toggle(this.dqaOptions.errors[key]);
			}
			for(var key in this.dqaOptions.warnings){
				if(!this.isSelected(this.dqaOptions.warnings[key]))
					this.toggle(this.dqaOptions.warnings[key]);
			}
		}
		
		this.clearAll = function(){
			dqaF.filters = [];
		}
			
        this.dqaOptions = {
                errors: [
//                    {code: '115', desc: 'Patient birth date is after submission'},
//                    {code: '116', desc: 'Patient birth date is in future'},
//                    {code: '129', desc: 'Patient death date is before birth'},
//                    {code: '130', desc: 'Patient death date is in future'},
//                    {code: '491', desc: 'Vaccination admin code is invalid for date administered'},
//                    {code: '257', desc: 'Vaccination admin date is before or after licensed vaccine range'},
//                    {code: '252', desc: 'Vaccination admin date is after message submitted'},
//                    {code: '253', desc: 'Vaccination admin date is after patient death date'},
//                    {code: '254', desc: 'Vaccination admin date is after system entry date'},
//                    {code: '255', desc: 'Vaccination admin date is before birth'},
//                    {code: '259', desc: 'Vaccination admin date is before or after when valid for patient age'},
//                    {code: '266', desc: 'Vaccination admin date end is different from start date'},
                    {code: '115', desc: 'Patient birth date is after submission'},
                    {code: '116', desc: 'Patient birth date is in future'},
                    {code: '129', desc: 'Patient death date is before birth'},
                    {code: '130', desc: 'Patient death date is in future'},
                    {code: '491', desc: 'Vaccination admin code is invalid for date administered'},
                    {code: '257', desc: 'Vaccination admin date is before or after licensed vaccine range'},
                    {code: '252', desc: 'Vaccination admin date is after message submitted'},
                    {code: '253', desc: 'Vaccination admin date is after patient death date'},
                    {code: '254', desc: 'Vaccination admin date is after system entry date'},
                    {code: '255', desc: 'Vaccination admin date is before birth'},
                    {code: '266', desc: 'Vaccination admin date end is different from start date'}
                ],
                warnings: [
//                    {code: '327', desc: 'Vaccination information source is administered but appears to historical'},
//                    {code: '329', desc: 'Vaccination information source is historical but appears to be administered'},
//                    {code: '256', desc: 'Vaccination admin date is before or after expected vaccine usage range'},
//                    {code: '258', desc: 'Vaccination admin date is before or after when expected for patient age'},
//                    {code: '268', desc: 'Vaccination administered amount is invalid'},
//                    {code: '276', desc: 'Vaccination body route is invalid for vaccine indicated'},
//                    {code: '610', desc: 'Vaccination body route is invalid for body site indicated'},
//                    {code: '305', desc: 'Vaccination CVX code is deprecated '},
//                    {code: '328', desc: 'Vaccination information source is deprecated'},
                    {code: '105', desc: 'Patient address state is deprecated'},
                    {code: '107', desc: 'Patient address state is invalid'},
                    {code: '109', desc: 'Patient address state is unrecognized'},
                    {code: '112', desc: 'Patient address zip is invalid'},
                    {code: '95', desc: 'Patient address country is deprecated'},
                    {code: '120', desc: 'Patient birth date is very long ago'},
                    {code: '133', desc: 'Patient death indicator is inconsistent'},
                    {code: '135', desc: 'Patient ethnicity is deprecated'},
                    {code: '143', desc: 'Patient gender is deprecated'},
                    {code: '156', desc: 'Patient guardian name is same as underage patient'},
                    {code: '580', desc: "Patient mother's maiden name has junk name"},
                    {code: '581', desc: "Patient mother's maiden name has invalid prefixes"},
                    {code: '546', desc: "Patient mother's maiden name is unexpectedly short"},
                    {code: '172', desc: 'Patient name may be temporary newborn name'},
                    {code: '173', desc: 'Patient name may be test name'},
                    {code: '578', desc: 'Patient name has junk name'},
                    {code: '405', desc: 'Patient name type code is deprecated '},
                    {code: '174', desc: 'Patient phone is incomplete'},
                    {code: '453', desc: 'Patient phone tel use code is deprecated'},
                    {code: '458', desc: 'Patient phone tel equip code is deprecated'},
                    {code: '177', desc: 'Patient primary facility id is deprecated'},
                    {code: '183', desc: 'Patient primary language is deprecated'},
                    {code: '188', desc: 'Patient primary physician id is deprecated'},
                    {code: '194', desc: 'Patient protection indicator is deprecated'},
                    {code: '201', desc: 'Patient publicity code is deprecated'},
                    {code: '206', desc: 'Patient race is deprecated '},
                    {code: '213', desc: 'Patient registry status is deprecated'},
                    {code: '232', desc: 'Vaccination action code is deprecated'},
                    {code: '327', desc: 'Vaccination information source is administered but appears to historical'},
                    {code: '329', desc: 'Vaccination information source is historical but appears to be administered'},
                    {code: '256', desc: 'Vaccination admin date is before or after expected vaccine usage range'},
                    {code: '258', desc: 'Vaccination admin date is before or after when expected for patient age '},
                    {code: '268', desc: 'Vaccination administered amount is invalid '},
                    {code: '93', desc: 'Patient address city is invalid'},
                    {code: '142', desc: 'Patient name first may include middle initial'},
                    {code: '140', desc: 'Patient name first is invalid'}
                ]
            };
	}]);
	
	app.directive("profileSelection", ['DQAFilters','$http','toolkit','model','config','ConfigObject','general','$uibModal',function(dqaF,$http,toolkit,model,config,co,gen,$uibModal) {
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
				this.dqa    = true;
				var ctrl = this;		
				
		        this.openDQA = function (size) {

		            var modalInstance = $uibModal.open({
			              animation: false,
			              templateUrl: 'myModalContent.html',
			              controller : 'DQAController',
			              controllerAs : "ctrl"
		              });
		            
		            modalInstance.result.then(function () {
		                
		              }, function () {
		                
		              });
		        };
		        
		        this.nbFilters = function(){
		        	return dqaF.filters.length;
		        }
		        
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
					console.log(pro.dqa);
					this.dqa = (pro.dqa === "allow");
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
	
	app.directive("quickTesting", ['toolkit','model','config','ConfigObject','general','$http',function(toolkit,model,config,co,general,$http) {
		return {
			restrict: "E",
			templateUrl: "directives_templates/quick_t.html",
			controllerAs: "q",
			controller: function() {
				this.oid = "";
				this.content = "";
				var ctrl = this;
				
				this.showValidate = function(){
					return ctrl.oid != "" && ctrl.content != "";
				};
				
				this.validate = function(){
						co.message = ctrl.content;
						co.params = {
								profile : ctrl.oid,
								domain  : "",
								tables	: "",
								context : ""
						};
						
						toolkit.validate().then(function(data){
							general.response.type = data.data.type;
								general.response.content = data.data.content;
							
							general.time = data.data.time;
							var blob = new Blob([data.data.xml],{type: 'text/xml'});
							general.response.xml = URL.createObjectURL(blob);
							console.log("XML : "+general.response.xml);
							general.tab = 3;
							ctrl.content = "";
							ctrl.response = "";
							ctrl.files = null;
							ctrl.isBatch = false;
							ctrl.filename = "";
							ctrl.notZIP = false;
						});
					};
			}
		}
	}]);
	
	app.directive("messageArea", ['DQAFilters','toolkit','model','config','ConfigObject','general','$http',function(dqaF,toolkit,model,config,co,general,$http) {
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
						co.params.dqa = (dqaF.filters.length != 0);
						co.params.dqaFilters = "";
						if(co.params.dqa){
							for(var key in dqaF.filters){
								co.params.dqaFilters = co.params.dqaFilters + dqaF.filters[key] +":";
							}
						}
						toolkit.validate().then(function(data){
							general.response.type = data.data.type;
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
						
						co.params.dqa = (dqaF.filters.length != 0);
						co.params.dqaFilters = "";
						if(co.params.dqa){
							for(var key in dqaF.filters){
								co.params.dqaFilters = co.params.dqaFilters + dqaF.filters[key] +":";
							}
						}
						
						$http.post("batch",{
							profile : co.params.profile,
							domain  : co.params.domain,
							tables	: co.params.tables,
							contextB : co.params.context,
							dqa		: co.params.dqa,
							dqaFilters : co.params.dqaFilters,
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
