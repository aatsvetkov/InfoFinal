var socket = new WebSocket("ws://" + window.location.host + "/socket");
var numResp = null;
socket.onmessage = function(event) {
    var messageJson = JSON.parse(event.data);
    if(numResp===null){
        numResp = messageJson.count;
    } else {
        numResp++;
    }
    $('.countresp').remove();
    var content = ' <span class="countresp numberCircle">'+ numResp +'</span>';
    $('#resps').append(content);
};

