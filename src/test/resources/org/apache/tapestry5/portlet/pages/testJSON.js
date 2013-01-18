Tapestry.Initializer.testJSON = function(elementId){
    $(elementId).observe("click", function(event){

        Tapestry.ajaxRequest($(elementId).href, function(response){
            alert(response.responseJSON.message);
        });

        event.preventDefault();
    });
};