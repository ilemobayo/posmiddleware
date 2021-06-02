# POS Middleware

This project helps separate the module that communicates directly with NSC/Switch from the POS application/SDK API.

# Development Requirements
1. Java
2. SpringBoot
3. ISO8583 parser
4. Socket Communication


### POST BODY
`{
	"msgType":"0200",
    "field2": "5399________230",
    "field3": "000000",
    "field4": "000000001000",
    "field7": "1216135321",
    "field11": "582387",
    "field12": "135256",
    "field13": "1216",
    "field14": "2407",
    "field18": "2012",
    "field22": "051",
    "field23": "001",
    "field25": "00",
    "field26": "04",
    "field28": "C00000000",
    "field32": "62778700000",
    "field33": "",
    "field35": "5399235051404532D2407221014666459F",
    "field37": "74927772321",
    "field39": "",
    "field40": "221",
    "field41": "6098CFL9",
    "field42": "2058FC020111905",
    "field43": "FC           LANG",
    "field49": "566",
    "field53": "",
    "field55": "9F26087021159357AE885459F5A0853992350514052495F341019F2103135230303003133939",
    "field63": "",
    "field64": "",
    "field123": "911101713334121",
    "field128": "13F2E0D19CFD10232386D840899118FE5A84339C278B10057D3A5382E8A9CD09"
}`
