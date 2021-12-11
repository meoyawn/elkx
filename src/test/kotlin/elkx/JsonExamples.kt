package elkx

const val EX1 = """
{
  "id": "G1",
  "children": [
    {
      "id": "N1",
      "labels": [
        {
          "text": "TimedPlotter",
          "id": "L1",
          "width": 74.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P1",
          "properties": {
            "port.side": "WEST",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "properties": {
        "portConstraints": "FIXED_ORDER",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "width": 41.0,
      "height": 41.0
    },
    {
      "id": "N2",
      "labels": [
        {
          "text": "population",
          "id": "L2",
          "width": 60.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P2",
          "properties": {
            "port.side": "EAST",
            "port.index": "0"
          }
        },
        {
          "id": "P3",
          "properties": {
            "port.side": "WEST",
            "port.index": "-1"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "children": [
        {
          "id": "N3",
          "labels": [
            {
              "text": "growthAddSubtract",
              "id": "L3",
              "width": 110.0,
              "height": 15.0
            }
          ],
          "ports": [
            {
              "id": "P4",
              "properties": {
                "port.side": "WEST",
                "port.index": "0"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P5",
              "properties": {
                "port.side": "WEST",
                "port.index": "-1"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P6",
              "properties": {
                "port.side": "EAST",
                "port.index": "2"
              },
              "width": 8.0,
              "height": 8.0
            }
          ],
          "properties": {
            "portConstraints": "FIXED_ORDER",
            "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
          },
          "width": 41.0,
          "height": 41.0
        },
        {
          "id": "N4",
          "labels": [
            {
              "text": "Limit",
              "id": "L4",
              "width": 28.0,
              "height": 15.0
            }
          ],
          "ports": [
            {
              "id": "P7",
              "properties": {
                "port.side": "EAST",
                "port.index": "0"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P8",
              "properties": {
                "port.side": "WEST",
                "port.index": "-1"
              },
              "width": 8.0,
              "height": 8.0
            }
          ],
          "properties": {
            "portConstraints": "FIXED_ORDER",
            "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
          },
          "width": 238.0,
          "height": 25.0
        },
        {
          "id": "N5",
          "labels": [
            {
              "text": "Integrator",
              "id": "L5",
              "width": 55.0,
              "height": 15.0
            }
          ],
          "ports": [
            {
              "id": "P9",
              "properties": {
                "port.side": "SOUTH",
                "port.index": "0"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P10",
              "properties": {
                "port.side": "WEST",
                "port.index": "-1"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P11",
              "properties": {
                "port.side": "EAST",
                "port.index": "2"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P12",
              "properties": {
                "port.side": "SOUTH",
                "port.index": "-3"
              },
              "width": 8.0,
              "height": 8.0
            }
          ],
          "properties": {
            "portConstraints": "FIXED_ORDER",
            "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
          },
          "width": 44.0,
          "height": 46.0
        },
        {
          "id": "N6",
          "labels": [
            {
              "text": "growth",
              "id": "L6",
              "width": 39.0,
              "height": 15.0
            }
          ],
          "ports": [
            {
              "id": "P13",
              "properties": {
                "port.side": "EAST",
                "port.index": "0"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P14",
              "properties": {
                "port.side": "WEST",
                "port.index": "-1"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P15",
              "properties": {
                "port.side": "WEST",
                "port.index": "-2"
              },
              "width": 8.0,
              "height": 8.0
            }
          ],
          "properties": {
            "portConstraints": "FIXED_ORDER",
            "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
          },
          "width": 328.0,
          "height": 25.0
        }
      ],
      "properties": {
        "portConstraints": "FREE",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "edges": [
        {
          "id": "E11",
          "sources": [
            "P3"
          ],
          "targets": [
            "P15"
          ]
        },
        {
          "id": "E12",
          "sources": [
            "P6"
          ],
          "targets": [
            "P10"
          ]
        },
        {
          "id": "E13",
          "sources": [
            "P7"
          ],
          "targets": [
            "P5"
          ]
        },
        {
          "id": "E14",
          "sources": [
            "P11"
          ],
          "targets": [
            "P2"
          ]
        },
        {
          "id": "E15",
          "sources": [
            "P11"
          ],
          "targets": [
            "P8"
          ]
        },
        {
          "id": "E16",
          "sources": [
            "P11"
          ],
          "targets": [
            "P14"
          ]
        },
        {
          "id": "E17",
          "sources": [
            "P13"
          ],
          "targets": [
            "P4"
          ]
        }
      ]
    },
    {
      "id": "N7",
      "labels": [
        {
          "text": "HeatProduction",
          "id": "L7",
          "width": 90.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P16",
          "properties": {
            "port.side": "WEST",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P17",
          "properties": {
            "port.side": "EAST",
            "port.index": "1"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "children": [
        {
          "id": "N8",
          "labels": [
            {
              "text": "Expression",
              "id": "L8",
              "width": 66.0,
              "height": 15.0
            }
          ],
          "ports": [
            {
              "id": "P18",
              "properties": {
                "port.side": "EAST",
                "port.index": "0"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P19",
              "properties": {
                "port.side": "WEST",
                "port.index": "-2"
              },
              "width": 8.0,
              "height": 8.0
            }
          ],
          "properties": {
            "portConstraints": "FIXED_ORDER",
            "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
          },
          "width": 243.0,
          "height": 25.0
        }
      ],
      "properties": {
        "portConstraints": "FREE",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "edges": [
        {
          "id": "E18",
          "sources": [
            "P16"
          ],
          "targets": [
            "P19"
          ]
        },
        {
          "id": "E19",
          "sources": [
            "P18"
          ],
          "targets": [
            "P17"
          ]
        }
      ]
    },
    {
      "id": "N9",
      "labels": [
        {
          "text": "HeatExchanger",
          "id": "L9",
          "width": 90.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P20",
          "properties": {
            "port.side": "WEST",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P21",
          "properties": {
            "port.side": "WEST",
            "port.index": "-1"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P22",
          "properties": {
            "port.side": "EAST",
            "port.index": "2"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P23",
          "properties": {
            "port.side": "WEST",
            "port.index": "-3"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "children": [
        {
          "id": "N10",
          "labels": [
            {
              "text": "HeatExchanger",
              "id": "L10",
              "width": 90.0,
              "height": 15.0
            }
          ],
          "ports": [
            {
              "id": "P24",
              "properties": {
                "port.side": "EAST",
                "port.index": "0"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P25",
              "properties": {
                "port.side": "WEST",
                "port.index": "-2"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P26",
              "properties": {
                "port.side": "WEST",
                "port.index": "-3"
              },
              "width": 8.0,
              "height": 8.0
            },
            {
              "id": "P27",
              "properties": {
                "port.side": "WEST",
                "port.index": "-4"
              },
              "width": 8.0,
              "height": 8.0
            }
          ],
          "properties": {
            "portConstraints": "FIXED_ORDER",
            "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
          },
          "width": 181.0,
          "height": 25.0
        }
      ],
      "properties": {
        "portConstraints": "FREE",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "edges": [
        {
          "id": "E20",
          "sources": [
            "P21"
          ],
          "targets": [
            "P27"
          ]
        },
        {
          "id": "E21",
          "sources": [
            "P20"
          ],
          "targets": [
            "P26"
          ]
        },
        {
          "id": "E22",
          "sources": [
            "P23"
          ],
          "targets": [
            "P25"
          ]
        },
        {
          "id": "E23",
          "sources": [
            "P24"
          ],
          "targets": [
            "P22"
          ]
        }
      ]
    },
    {
      "id": "N11",
      "labels": [
        {
          "text": "AddSubtract",
          "id": "L11",
          "width": 72.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P28",
          "properties": {
            "port.side": "WEST",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P29",
          "properties": {
            "port.side": "WEST",
            "port.index": "-1"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P30",
          "properties": {
            "port.side": "EAST",
            "port.index": "2"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "properties": {
        "portConstraints": "FIXED_ORDER",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "width": 41.0,
      "height": 41.0
    },
    {
      "id": "N12",
      "labels": [
        {
          "text": "Integrator",
          "id": "L12",
          "width": 55.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P31",
          "properties": {
            "port.side": "SOUTH",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P32",
          "properties": {
            "port.side": "WEST",
            "port.index": "-1"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P33",
          "properties": {
            "port.side": "EAST",
            "port.index": "2"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P34",
          "properties": {
            "port.side": "SOUTH",
            "port.index": "-3"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "properties": {
        "portConstraints": "FIXED_ORDER",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "width": 44.0,
      "height": 46.0
    },
    {
      "id": "N13",
      "labels": [
        {
          "text": "TempCW",
          "id": "L13",
          "width": 53.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P35",
          "properties": {
            "port.side": "EAST",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P36",
          "properties": {
            "port.side": "WEST",
            "port.index": "-1"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "properties": {
        "portConstraints": "FIXED_ORDER",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "width": 29.0,
      "height": 25.0
    },
    {
      "id": "N14",
      "labels": [
        {
          "text": "flow rate",
          "id": "L14",
          "width": 49.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P37",
          "properties": {
            "port.side": "EAST",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P38",
          "properties": {
            "port.side": "WEST",
            "port.index": "-1"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "properties": {
        "portConstraints": "FIXED_ORDER",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "width": 32.0,
      "height": 25.0
    },
    {
      "id": "N15",
      "labels": [
        {
          "text": "Convert Population to Joule",
          "id": "L15",
          "width": 158.0,
          "height": 15.0
        }
      ],
      "ports": [
        {
          "id": "P39",
          "properties": {
            "port.side": "WEST",
            "port.index": "0"
          },
          "width": 8.0,
          "height": 8.0
        },
        {
          "id": "P40",
          "properties": {
            "port.side": "EAST",
            "port.index": "2"
          },
          "width": 8.0,
          "height": 8.0
        }
      ],
      "properties": {
        "portConstraints": "FIXED_ORDER",
        "nodeLabels.placement": "[H_LEFT, V_TOP, OUTSIDE]"
      },
      "width": 14.0,
      "height": 25.0
    }
  ],
  "properties": {
    "portConstraints": "FREE"
  },
  "edges": [
    {
      "id": "E1",
      "sources": [
        "P2"
      ],
      "targets": [
        "P1"
      ]
    },
    {
      "id": "E2",
      "sources": [
        "P2"
      ],
      "targets": [
        "P40"
      ]
    },
    {
      "id": "E3",
      "sources": [
        "P17"
      ],
      "targets": [
        "P28"
      ]
    },
    {
      "id": "E4",
      "sources": [
        "P22"
      ],
      "targets": [
        "P29"
      ]
    },
    {
      "id": "E5",
      "sources": [
        "P30"
      ],
      "targets": [
        "P32"
      ]
    },
    {
      "id": "E6",
      "sources": [
        "P33"
      ],
      "targets": [
        "P3"
      ]
    },
    {
      "id": "E7",
      "sources": [
        "P33"
      ],
      "targets": [
        "P20"
      ]
    },
    {
      "id": "E8",
      "sources": [
        "P35"
      ],
      "targets": [
        "P21"
      ]
    },
    {
      "id": "E9",
      "sources": [
        "P37"
      ],
      "targets": [
        "P23"
      ]
    },
    {
      "id": "E10",
      "sources": [
        "P39"
      ],
      "targets": [
        "P16"
      ]
    }
  ]
}
"""
