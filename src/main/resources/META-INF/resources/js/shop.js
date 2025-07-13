const appvalues = document.querySelector('#appvalues');
const storeId = appvalues.dataset.storeId;
const streamUrl = appvalues.dataset.streamUrl;
const loyaltyStreamUrl = appvalues.dataset.loyaltyStreamUrl;
const rewardUrl = appvalues.dataset.rewardUrl;

// Rewards Stream EventSource - ポイント表示
const rewardPointsEl = document.getElementById('rewardPoints');

/* Display the modal popup with selected data */
$('#myModal').on('show.bs.modal', function (event) {

    // hide the alert for successfully adding an item
    $('#item_added_alert').hide();

    var button = $(event.relatedTarget)
    var item = button.data('whatever')
    var item_display_name = button.data('display_name');
    var item_type = button.data('item_type');
    var item_price = button.data('item_price');
    var modal = $(this);
    //        modal.find('.modal-body inplut').val(recipient)
    modal.find('#item_label').text(item_display_name);
    modal.find('#item').val(item);
    modal.find('#item_type').val(item_type);
    modal.find('#item_price').val(item_price);
    displayCurrentOrder();
});

/* Display the current order */
function displayCurrentOrder(){
    // clear out the list
    $('#current_order').empty();

    // get current items
    let qdca10 = $('#order_form').find('input[name="qdca10"]').serializeArray();
    let qdca10pro = $('#order_form').find('input[name="qdca10pro"]').serializeArray();
    let current_items = qdca10.concat(qdca10pro);

    if(current_items.length >= 1){
        current_items.reverse().forEach(function(e){
            let order = JSON.parse(e.value);
            $('#current_order').append(`
                <li class="borderless input-shop" style="list-style: none; margin-left: 100px;">
                    ${displayFriendlyItem(order.item)} for ${order.name}
                </li>
            `);
        });
    }
}

/* Update the current order after a new item is added */
function updateCurrentOrder(item, name) {
    let new_line = $(`
        <li style="list-style: none; margin-left: 100px;">
            ${displayFriendlyItem(item)} for ${name}
        </li>
    `).hide().fadeIn(500);
    $('#current_order').append(new_line);
}
/* Modal popup - add item to the order*/
$('#item_form').submit(function( event ) {

    let item_type = $('#item_form').find('input[name="item_type"]').val();
    let item = $('#item_form').find('input[name="item"]').val();
    let name = $('#item_form').find('input[name="name"]').val();
    let price = parseFloat($('#item_form').find('input[name="item_price"]').val());

    let item_to_add = { 'item' : item, 'name': name, 'price': price};

    if(item_type == 'qdca10'){
        console.log('adding qdca10 item');
        let qdca10 = $('#order_form').find('input[name="qdca10"]').serializeArray();
        if(qdca10.length === 0){
            $('<input>').attr({
                type: 'hidden',
                id: 'qdca10-' + qdca10.length,
                name: 'qdca10',
                value: JSON.stringify(item_to_add)
            }).appendTo('#order_form');
        }else if(qdca10.length >= 1){
            $('<input>').attr({
                type: 'hidden',
                id: 'qdca10-' + qdca10.length++,
                name: 'qdca10',
                value: JSON.stringify(item_to_add)
            }).appendTo('#order_form');
        }
        console.log('added');

    }else if(item_type == 'qdca10pro'){
        console.log('adding qdca10pro item');
        var qdca10pro = $('#order_form').find('input[name="qdca10pro"]').serializeArray();
        if(qdca10pro.length === 0){
            $('<input>').attr({
                type: 'hidden',
                id: 'qdca10pro-' + qdca10pro.length,
                name: 'qdca10pro',
                value: JSON.stringify(item_to_add)
            }).appendTo('#order_form');
        }else if(qdca10pro.length >= 1){
            $('<input>').attr({
                type: 'hidden',
                id: 'qdca10pro-' + qdca10pro.length++,
                name: 'qdca10pro',
                value: JSON.stringify(item_to_add)
            }).appendTo('#order_form');
        }
        console.log('added');
    }

    // display the alert for successfully adding an item
    $('#item_added_alert').text(displayFriendlyItem(item) + ' for ' + name + ' added to order.').show();
    setTimeout(function(){
        updateCurrentOrder(item, name);
    }, 2000);
    setTimeout(function(){
        $('#item_added_alert').fadeOut(1000);
    }, 3000);

    event.preventDefault();
});

/* Modal popup - submit order */
$("#order_form").submit(function(event){
    console.log("order submitted");
    let qdca10 = $('#order_form').find('input[name="qdca10"]').map(function(){
        return JSON.parse($(this).val());
    }).get();
    let qdca10pro = $('#order_form').find('input[name="qdca10pro"]').map(function(){
        return JSON.parse($(this).val());
    }).get();

    // if the user clicks "Place Order" before "Add"
    if((qdca10 === undefined || qdca10.length == 0 ) && (qdca10pro === undefined || qdca10pro.length == 0)){

        let item_type = $('#item_form').find('input[name="item_type"]').val();
        let item = $('#item_form').find('input[name="item"]').val();
        let name = $('#item_form').find('input[name="name"]').val();
        let price = parseFloat($('#item_form').find('input[name="item_price"]').val());
        let item_to_add = { 'name': name, 'item': item, 'price': price };

        if(item_type == 'beverage'){
            console.log('adding beverage');
            qdca10.push(item_to_add);
        }else if(item_type == 'qdca10pro'){
            qdca10pro.push(item_to_add);
        }

    }

    let rewards_id = $('#rewards_id').val();

    let order = {};
    order.commandType = 'PLACE_ORDER';
    order.id = uuidv4();
    order.orderSource = 'WEB';
    order.storeId = storeId;
    order.rewardsId = rewards_id;
    order.qdca10Items = [];
    order.qdca10proItems = []

    if(qdca10.length >= 1){
        for (i = 0; i < qdca10.length; i++) {
            console.log(qdca10[i]);
            order.qdca10Items.push(qdca10[i]);
        }
    }
    if(qdca10pro.length >= 1){
        for (i = 0; i < qdca10pro.length; i++) {
            console.log(qdca10pro[i]);
            order.qdca10proItems.push(qdca10pro[i]);
        }
    }

    console.log("Sending order from web client: " + JSON.stringify(order));

    var jqxhr = $.ajax({
        beforeSend: function(xhrObj){
            xhrObj.setRequestHeader("Content-Type","application/json");
            xhrObj.setRequestHeader("Accept","application/json");
        },
        type: "POST",
        url: "/order",
        data: JSON.stringify(order),
        success: function(){ console.log('success for ' + JSON.stringify(order)); },
        contentType: 'application/json',
        dataType: 'json'
    })
        .done(function() {
            console.log( "done" );
            $('#order_form').find('input[name="qdca10"]').map(function () {
                $(this).remove();
            })
            $('#order_form').find('input[name="qdca10pro"]').map(function () {
                $(this).remove();
            })
        })
        .fail(function(jqxhr, errorStatus, errorThrown) {
            console.log( "error : " + errorThrown + " " + errorStatus);
        })
        .always(function() {
            console.log( "finished" );
        });

    // close the modal
    $('#myModal').modal('toggle');

    // prevent form submission
    event.preventDefault();
});

function mapToObjectRec(m) {
    let lo = {}
    for(let[k,v] of m) {
        if(v instanceof Map) {
            lo[k] = mapToObjectRec(v)
        }
        else {
            lo[k] = v
        }
    }
    return lo
}

// create a uuid for the order id
function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

// Status Board
$(function () {
    /* var source = new EventSource("http://quarkus-shop-web-quarkus-shop.apps.cluster-virtual-1b57.virtual-1b57.sandbox1482.opentlc.com/dashboard/stream"); */

    var source = new EventSource(streamUrl);
    source.onmessage = function(e) {
        console.log(e);
        var state = JSON.parse(e.data);
        if(state.status=="IN_PROGRESS")
            $("tbody").append(line(state));
        if(state.status=="FULFILLED"){40
            console.log(state);
//              $("#"+state.itemId).replaceWith(line(state));
//              setTimeout(cleanup(state.itemId), 15000);
            display(state);
        }
    };

    // Loyalty toast notification
    var loyaltySource = new EventSource(loyaltyStreamUrl);
    loyaltySource.onmessage = function(e) {
        console.log(e);
        var localtyReward = JSON.parse(e.data);
        var rewardTest = "You have won a free " + localtyReward.reward + "!"

        toastr.options = {
            "closeButton": false,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-top-right",
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "15000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }

        toastr["success"](rewardTest)
    };

});

function display(state){
    let count = (Math.floor(Math.random() * 15) * 1000) + 5000;
    console.log(count);
    $("#"+state.itemId).replaceWith(line(state));
    setTimeout(function(){ $("#"+state.itemId).remove(); }, count);
}

function cleanup(itemid){
    console.log("time to cleanup" + itemid);
//      $("#"+itemid).remove();
}

function line(state) {
    var orderId = state.orderId;
    var id = state.itemId;
    var product = state.item;
    var customer = state.name;
    var status = state.status;
    var preparedBy = state.madeBy;
    /*
      if (state.item) {
          qdca10 = state.item.preparedBy;
      }
    */
    return "<tr id='" + id + "'>" +
        "<td>" + customer + "</td>" +
        "<td>" + displayFriendlyItem(product) + "</td>" +
        "<td>" + displayFriendlyStatus(status) + "</td>" +
        "<td>" + displayFriendlyPreparedBy(preparedBy) + "</td></tr>";
}

/* Display friendly prepared by or nothing */
function displayFriendlyPreparedBy(value){
    return value === undefined ? ""
        : value === null ? ""
        : value === 'null' ? ""
        : value;
}

/* Display friendly product names
*/
function displayFriendlyItem(item){
    let result;

    switch(item){
        // qdca10
        case "QDC-A101":
            console.log("QDC-A101");
            result = "QDC-A101"
            break;
        case "CQDC-A102":
            console.log("CQDC-A102");
            result = "CQDC-A102";
            break;
        case "CQDC-A103":
            console.log("CQDC-A103");
            result = "CQDC-A103";
            break;
        case "QDC-A104-AC":
            console.log("QDC-A104-AC");
            result = "QDC-A104-AC";
            break;
        case "QDC-A104-AT":
            console.log("QDC-A104-AT");
            result = "QDC-A104-AT";
            break;
        // qdca10pro
        case "QDC-A105-Pro01":
            console.log("QDC-A105-Pro01");
            result = "QDC-A105-Pro01";
            break;
        case "QDC-A105-Pro02":
            console.log("QDC-A105-Pro02");
            result = "QDC-A105-Pro02";
            break;
        case "QDC-A105-Pro03":
            console.log("QDC-A105-Pro03");
            result = "QDC-A105-Pro03";
            break;
        case "QDC-A105-Pro04":
            console.log("QDC-A105-Pro04");
            result = "QDC-A105-Pro04";
            break;
        // default to the returned value
        default:
            result = item;
            console.log(item);
    }
    return result;
}

function displayFriendlyStatus(status){
    let result;
    switch(status){
        case "IN_PROGRESS":
            console.log("In progress");
            result = "In progress";
            break;
        case "FULFILLED":
            console.log("Ready!");
            result = "Ready";
            break;
        default:
            result = status;
    }
    return result;
}

function displayRewardPoint(){
    const rewardPointsEl = document.getElementById('rewardPoints');
    const rewardEventSource = new EventSource(rewardUrl);

    const accumulatedRewards = {};

    rewardEventSource.onmessage = function(event) {
        const reward = JSON.parse(event.data);
        console.log("reward point", reward);

        if (reward && reward.customerName && typeof reward.rewardAmount === "number") {
            const name = reward.customerName;

            if (!accumulatedRewards[name]) {
                accumulatedRewards[name] = 0;
            }
            accumulatedRewards[name] += reward.rewardAmount;

            const displayText = Object.entries(accumulatedRewards)
                .map(([customerName, points]) => `POINT ${points.toFixed(2)} `)
                .join(' | ');

            if (rewardPointsEl) {
                rewardPointsEl.textContent = displayText;
            }
        }
    };

    rewardEventSource.onerror = function(error) {
        console.error("SSE Error:", error);
    };
}

$('#rewards_modal').on('submit', function() {

    let rewards_id = $('#rewards_id').val();
    console.log("rewards email entered: " + rewards_id);
    $('#rewards_display_id').text(rewards_id);
    $.cookie('rewards_email',rewards_id, 10);
    $('#btn_cancel').click();
});

$('#rewardsModal').on('shown.bs.modal', function() {
    $('#rewards_id').focus();
});

$( document ).ready(function() {
    let email = $.cookie('rewards_email');
    if (email !== undefined){
        $('#rewards_id').val(email);
        $('#rewards_display_id').text(email);
    }
    displayRewardPoint();
});