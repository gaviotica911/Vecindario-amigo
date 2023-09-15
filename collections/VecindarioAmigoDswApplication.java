{
	"info": {
		"_postman_id": "8cb7333b-6b80-47df-80d3-a3e0ee796843",
		"name": "Vecino",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "29200318"
	},
	"item": [
		{
			"name": "Crear un vecino",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n\"nombre\": \"Gabriela\",\r\n\"edad\": 19,\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\r\n\"descripcion\": \"Estudio ISIS\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "{{baseURL}}/vecinos",
					"host": [
						"{{baseURL}}"
					],
					"path": [
						"vecinos"
					]
				}
			},
			"response": [
				{
					"name": "Crear un vecino",
					"originalRequest": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\"nombre\": \"Gabriela\"\r\n\"edad\": 19\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\"\r\n\"descripcion\": \"Estudio ISIS\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{baseURL}}/vecinos",
							"host": [
								"{{baseURL}}"
							],
							"path": [
								"vecinos"
							]
						}
					},
					"status": "Created",
					"code": 201,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json",
							"name": "Content-Type",
							"description": "",
							"type": "text"
						}
					],
					"cookie": [],
					"body": "{\r\n    \"id\": 1,\r\n\"nombre\": \"Gabriela\",\r\n\"edad\": 19,\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\r\n\"descripcion\": \"Estudio ISIS\"\r\n}"
				}
			]
		},
		{
			"name": "Obtener todos los vecinos",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "{{baseUrl}}/vecinos",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"vecinos"
					]
				}
			},
			"response": [
				{
					"name": "Obtener todos los vecinos",
					"originalRequest": {
						"method": "GET",
						"header": [
							{
								"key": "Content-Type",
								"name": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "[ {\r\n\"nombre\": \"Gabriela\"\r\n\"edad\": 19\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\"\r\n\"descripcion\": \"Estudio ISIS\"\r\n\"gruposDeInteres\":[]\r\n\"publicaciones\":[]\r\n\"vecindario\":[]\r\n}]",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{baseUrl}}/vecinos/{{vecino_id_1}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"vecinos",
								"{{vecino_id_1}}"
							]
						}
					},
					"status": "OK",
					"code": 200,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json",
							"name": "Content-Type",
							"description": "",
							"type": "text"
						}
					],
					"cookie": [],
					"body": "[ {\r\n\"nombre\": \"Gabriela\",\r\n\"edad\": 19,\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\r\n\"descripcion\": \"Estudio ISIS\",\r\n\"gruposDeInteres\":[],\r\n\"publicaciones\":[],\r\n\"vecindario\":[]\r\n}]"
				}
			]
		},
		{
			"name": "Obtener un vecino",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "{{baseUrl}}/vecinos/{{vecino_id_1}}",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"vecinos",
						"{{vecino_id_1}}"
					]
				}
			},
			"response": [
				{
					"name": "Obtener un vecino",
					"originalRequest": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/vecinos/{{vecino_id_1}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"vecinos",
								"{{vecino_id_1}}"
							]
						}
					},
					"status": "OK",
					"code": 200,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json",
							"name": "Content-Type",
							"description": "",
							"type": "text"
						}
					],
					"cookie": [],
					"body": "{\n    \"nombre\": \"Gabriela\",\n    \"edad\": 19,\n    \"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\n    \"descripcion\": \"Estudio ISIS\",\n    \"gruposDeInteres\": [],\n    \"publicaciones\": [],\n    \"vecindario\": []\n}"
				}
			]
		},
		{
			"name": "Obtener un vecino que no existe",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/vecinos/0",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"vecinos",
						"0"
					]
				}
			},
			"response": [
				{
					"name": "Obtener un vecino que no existe",
					"originalRequest": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/vecinos/0",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"vecinos",
								"0"
							]
						}
					},
					"status": "Not Found",
					"code": 404,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json",
							"name": "Content-Type",
							"description": "",
							"type": "text"
						}
					],
					"cookie": [],
					"body": "{\n    \"apierror\": {\n        \"status\": \"NOT_FOUND\",\n        \"timestamp\": \"21-02-2023 10:01:09\",\n        \"message\": \"The neighbor with the given id was not found\"\n    }\n}"
				}
			]
		},
		{
			"name": "Editar un vecino",
			"request": {
				"method": "PUT",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n\"nombre\": \"Gabriela\",\r\n\"edad\": 20,\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\r\n\"descripcion\": \"Estudio ISIS\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "{{baseUrl}}/vecinos/{{vecino_id_1}}",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"vecinos",
						"{{vecino_id_1}}"
					]
				}
			},
			"response": [
				{
					"name": "Editar un vecino",
					"originalRequest": {
						"method": "PUT",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    id: 1,\r\n\"nombre\": \"Gabriela\",\r\n\"edad\": 20,\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\r\n\"descripcion\": \"Estudio ISIS\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{baseUrl}}/vecinos/{{vecino_id_1}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"vecinos",
								"{{vecino_id_1}}"
							]
						}
					},
					"status": "OK",
					"code": 200,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json",
							"name": "Content-Type",
							"description": "",
							"type": "text"
						}
					],
					"cookie": [],
					"body": "{\n    \"id\": 1,\n    \"nombre\": \"Gabriela\",\n    \"edad\": 19,\n    \"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\n    \"descripcion\": \"Estudio ISIS\"\n}"
				}
			]
		},
		{
			"name": "Editar un autor que no existe",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n\"nombre\": \"Gabriela\",\r\n\"edad\": 20,\r\n\"profile_pic\": \"https://images.ecestaticos.com/EM_ympCtHYjjeP0kMvITDpiNPps=/2x25:1279x693/600x315/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F998%2Fded%2Ffbf%2F998dedfbfa6b8902025a38ec4fb19660.jpg\",\r\n\"descripcion\": \"Estudio ISIS\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "{{baseUrl}}/vecinos/0",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"vecinos",
						"0"
					]
				}
			},
			"response": [
				{
					"name": "Editar un autor que no existe",
					"originalRequest": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/vecinos/0",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"vecinos",
								"0"
							]
						}
					},
					"status": "Not Found",
					"code": 404,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json",
							"name": "Content-Type",
							"description": "",
							"type": "text"
						}
					],
					"cookie": [],
					"body": "{\n    \"apierror\": {\n        \"status\": \"NOT_FOUND\",\n        \"timestamp\": \"21-02-2023 10:04:10\",\n        \"message\": \"The neighbor  with the given id was not found\"\n    }\n}"
				}
			]
		},
		{
			"name": "Borrar un vecino",
			"request": {
				"method": "DELETE",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/vecinos/{{vecinos_id_1}}",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"vecinos",
						"{{vecinos_id_1}}"
					]
				}
			},
			"response": [
				{
					"name": "Borrar un vecino",
					"originalRequest": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/vecinos/{{vecinos_id_1}}",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"vecinos",
								"{{vecinos_id_1}}"
							]
						}
					},
					"status": "No Content",
					"code": 204,
					"_postman_previewlanguage": null,
					"header": null,
					"cookie": [],
					"body": null
				}
			]
		},
		{
			"name": "Borrar un vecino que no existe",
			"request": {
				"method": "DELETE",
				"header": [],
				"url": {
					"raw": "{{baseUrl}}/vecinos/0",
					"host": [
						"{{baseUrl}}"
					],
					"path": [
						"vecinos",
						"0"
					]
				}
			},
			"response": [
				{
					"name": "Borrar un vecino que no existe",
					"originalRequest": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{baseUrl}}/vecinos/0",
							"host": [
								"{{baseUrl}}"
							],
							"path": [
								"vecinos",
								"0"
							]
						}
					},
					"status": "Not Found",
					"code": 404,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json",
							"name": "Content-Type",
							"description": "",
							"type": "text"
						}
					],
					"cookie": [],
					"body": "{\n    \"apierror\": {\n        \"status\": \"NOT_FOUND\",\n        \"timestamp\": \"21-02-2023 10:05:19\",\n        \"message\": \"The neighbor with the given id was not found\"\n    }\n}"
				}
			]
		}
	]
}