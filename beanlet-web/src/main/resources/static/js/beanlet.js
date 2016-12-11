// handle ajax errors globally
$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
  logger.debug('ajax error: ' + thrownError);
  if (jqxhr.status == 403) {
    window.location = '/';
  }
});

var logger = {
  level: 'debug',
  debug: function (msg) {
    if (logger.level == 'debug') {
      console.log(msg);
    }
  }
};

var service = {
  calendarCache: {},
  getCalendar: function(year, month, responseHandler, useCache){
    var cacheKey = '' + year + month;
    if (useCache && service.calendarCache[cacheKey]) {
      responseHandler(service.calendarCache[cacheKey]);
      return;
    }
    ajax.get({
      url: window.location + '/calendar',
      data: {year:year, month:month}
    }, function(data){
      service.calendarCache[''+data.year+data.month] = data; // cache the calendar data
      responseHandler(data);
    });
  },
  getBeans: function (dateKey, responseHandler) {
    ajax.get({
      url: window.location + '/beans',
      data: {dateKey:dateKey}
    }, responseHandler);
  },
  addBean: function (beanletId, dateKey, responseHandler) {
    ajax.post({
      url: window.location + '/beans',
      data:{dateKey:dateKey}
    }, responseHandler);
  },
  changeBean: function (beanId, formValues, responseHandler) {
    ajax.put({
      url: window.location + '/beans/'+beanId,
      data: formValues
    }, responseHandler);
  },
  deleteBean: function (beanId, responseHandler) {
    ajax.delete({url: window.location + '/beans/'+beanId}, responseHandler);
  },
  changeBeanlet: function (beanlet, responseHandler) {
    ajax.put({ url: window.location, data: beanlet}, responseHandler);
  },
  deleteBeanlet: function (beanletId, responseHandler) {
    ajax.delete({ url: '/beanlets/' + beanletId }, responseHandler);
  }
};

var labels = {
  months: {
    1: 'January',
    2: 'February',
    3: 'March',
    4: 'April',
    5: 'May',
    6: 'June',
    7: 'July',
    8: 'August',
    9: 'September',
    10: 'October',
    11: 'November',
    12: 'December'
  }
};

var beanlet = {
  calendar: null,
  goToPreviousMonth: function () {
    if (!beanlet.calendar) return false;
    beanlet.hideBeans(function () {
      beanlet.disableFooterLinks();
      service.getCalendar(beanlet.calendar.previousYear, beanlet.calendar.previousMonth, beanlet.getCalendarResponseHandler, true);
    });
    return false;
  },
  goToCurrentMonth: function () {
    var currentDate = new Date();
    beanlet.hideBeans(function () {
      beanlet.disableFooterLinks();
      service.getCalendar(currentDate.getFullYear(), currentDate.getMonth()+1, beanlet.getCalendarResponseHandler, true);
    });
    return false;
  },
  goToNextMonth: function () {
    if (!beanlet.calendar) return false;
    beanlet.hideBeans(function () {
      beanlet.disableFooterLinks();
      service.getCalendar(beanlet.calendar.nextYear, beanlet.calendar.nextMonth, beanlet.getCalendarResponseHandler, true);
    });
    return false;
  },
  refreshCalendar: function () {
    logger.debug('refreshing calendar');
    var c = beanlet.calendar;
    if (!c) {
      logger.debug('beanlet.calendar is not there!');
      return false;
    }
    logger.debug('refreshing calendar using ' + c);
    beanlet.retainSelectedDay = true;
    service.getCalendar(c.year, c.month, beanlet.getCalendarResponseHandler);
  },
  initializeCalendarLinks: function () {
    $('#cal-prev').click(beanlet.goToPreviousMonth);
    $('#cal-today').click(beanlet.goToCurrentMonth);
    $('#cal-next').click(beanlet.goToNextMonth);
  },
  initializeCalendar: function () {
    beanlet.initializeCalendarLinks();
    $('#beans-table').find('td').click(beanlet.getBeans);
    service.getCalendar(null, null, beanlet.getCalendarResponseHandler);
  },
  showWeeks: function (numDays) {
    var weeksToShow = numDays / 7;
    for (var i = 0; i < weeksToShow; i++) {
      $('#week'+i).show();
    }
    for (var j = 5; j >= weeksToShow; j--) {
      $('#week'+j).hide();
    }
  },
  getCalendarResponseHandler: function(beanletCalendar) {
    if (!beanletCalendar) {
      logger.debug('response is ' + beanletCalendar + ', so exiting');
      return false;
    }
    beanlet.calendar = beanletCalendar;
    $('#label-month').text(labels.months[beanletCalendar.month]);
    $('#label-year').text(beanletCalendar.year);
    var days = beanletCalendar.days;
    beanlet.showWeeks(days.length);

    // clear "today"
    $('.today').removeClass('today');

    // clear "selected"
    if (!beanlet.retainSelectedDay) $('.selected').removeClass('selected');

    var day;
    for (var i=0; i < days.length; i++) {
      day = days[i];
      beanletCalendar[day.dateKey] = day; // add another attribute for quick access to a day given its dateKey...todo: move this to service call?

      var cell = $('#d'+i);

      cell.text(day.dayOfMonth).attr('dateKey', day.dateKey);

      if (day.currentMonth) {
        cell.removeClass('not-current');
      } else {
        cell.addClass('not-current');
      }

      if (day.today) {
        cell.addClass('today');
        // select today only if there's not already another one selected
        if (!beanlet.retainSelectedDay) {
          cell.addClass('selected');
        }
      }

      if (day.beanCount) {
        cell.addClass('bg-success');
      } else {
        cell.removeClass('bg-success');
      }
    }
    beanlet.retainSelectedDay = false; // reset this
    $('.selected').click();
  },
  getBeans: function () {
    $('.selected').removeClass('selected');
    var dateKey = $(this).addClass('selected').attr('datekey');
    beanlet.hideBeans(function () {
      service.getBeans(dateKey, beanlet.getBeansResponseHandler);
    });
  },
  hideBeans: function (callback) {
    $('#beans').hide('slide', {direction:'up'}, 100, function(){
      $(this).find('li').remove();
      if (typeof callback == 'function') callback();
    });
  },
  displayBeanModal: function () {
    var beanElement = $(this);
    beanlet.selectedBeanId = beanElement.attr('id');
    var parts = beanElement.attr('dt').split(' ');
    $('#bean-date').val(parts[0]).datepicker();
    $('#bean-hour').val(parts[1]);
    $('#bean-minute').val(parts[2]);
    $('#bean-ampm').val(parts[3]);
    $('#modal-bean').modal();
  },
  enableFooterLinks: function () {
    $('#footer').find('a').removeClass('link-disabled');
  },
  disableFooterLinks: function () {
    $('#footer').find('a').addClass('link-disabled');
  },
  getBeansResponseHandler: function (html) {
    var lis = $(html).find('li').click(beanlet.displayBeanModal);
    $('#beans').append(lis).show('slide', {direction:'up'}, 100);
    beanlet.enableFooterLinks();
  },
  addBean: function () {
    var selected = $('.selected');
    if (!selected.length) return false;
    var dateKey = selected.attr('dateKey');
    service.addBean(null, dateKey, beanlet.refreshCalendar);
    return false;
  },
  modifyBean: function () {
    var formValues = $('#form-modify-bean').serialize();
    service.changeBean(beanlet.selectedBeanId, formValues, beanlet.closeModalAndRefreshCalendar);
  },
  deleteBean: function () {
    service.deleteBean(beanlet.selectedBeanId, beanlet.closeModalAndRefreshCalendar);
  },
  saveBeanlet: function () {
    service.changeBeanlet({name:$('#beanlet-name').val()}, beanlet.changeBeanletResponseHandler);
  },
  changeBeanletResponseHandler: function (beanlet) {
    $('#modal-beanlet').modal('hide');
    $('#beanlet-name-header').text(beanlet.name);
  },
  deleteBeanlet: function () {
    if (!confirm('Delete beanlet?')) return false;
    var uri = window.location.pathname;
    var beanletId = uri.substring(uri.lastIndexOf('/')+1);
    service.deleteBeanlet(beanletId, beanlet.deleteBeanletResponseHandler);
  },
  deleteBeanletResponseHandler: function (deletedBeanlet) {
    window.location = '/beanlets';
  },
  closeModalAndRefreshCalendar: function (response) {
    beanlet.refreshCalendar();
    $('#modal-bean').modal('hide');
  },
  initializeFooterButtons: function () {
    $('#add-bean').click(beanlet.addBean);
  },
  initializeBeanModal: function () {
    $('#button-delete-bean').click(beanlet.deleteBean);
    $('#button-save-bean').click(beanlet.modifyBean);
  },
  initializeBeanletModal: function () {
    $('#button-save-beanlet').click(beanlet.saveBeanlet);
    $('#button-delete-beanlet').click(beanlet.deleteBeanlet);
  },
  initializePage: function () {
    beanlet.initializeCalendar();
    beanlet.initializeFooterButtons();
    beanlet.initializeBeanModal();
    beanlet.initializeBeanletModal();
  }
};

$(function () {
  logger.debug('initializing beanlet page');
  beanlet.initializePage();
});