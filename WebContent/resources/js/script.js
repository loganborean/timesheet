function initDialog() {
	 $("#createRowForm").dialog({
	     modal: true,
	     buttons: {
	            SomeButton: function () {
	                $("#justAButton").click();
	            },
	            Close: function () {
	                $(this).dialog("close");
	            }
	     },
	 });
	}
