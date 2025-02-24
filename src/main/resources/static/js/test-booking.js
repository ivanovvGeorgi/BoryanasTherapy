$(document).ready(function () {
    let fullyBookedDates = new Set();
    let unavailableDates = new Set();

    function disableWeekends(date) {
        return (date.getDay() === 0 || date.getDay() === 6);
    }

    function fetchFullyBookedDates() {
        return $.get('/public/fully-booked-dates')
            .then(function (data) {
                fullyBookedDates = new Set(data);
            })
            .catch(function (error) {
                console.error("Error fetching fully booked dates:", error);
            });
    }

    function fetchUnavailableDates() {
        return $.get('/public/unavailable-dates')
            .then(function (data) {
                unavailableDates = new Set(data);
            })
            .catch(function (error) {
                console.error("Error fetching unavailable dates:", error);
            });
    }

    // Fetch data before initializing Flatpickr
    Promise.all([fetchFullyBookedDates(), fetchUnavailableDates()]).then(() => {
        let flatpickrOptions = {
            inline: true,
            firstDayOfWeek: 1,
            dateFormat: "Y-m-d",
            minDate: new Date().fp_incr(1),
            locale: {
                firstDayOfWeek: 1,
            },
            disable: [...Array.from(fullyBookedDates), ...Array.from(unavailableDates), disableWeekends],
            onDayCreate: function (dObj, dStr, fp, dayElem) {
                let dateStr = new Date(dayElem.dateObj.getTime() - dayElem.dateObj.getTimezoneOffset() * 60000)
                    .toISOString().split('T')[0];
                let date = new Date(dateStr);
                let today = new Date();
                today.setHours(0, 0, 0, 0);

                if (fullyBookedDates.has(dateStr)) {
                    dayElem.classList.add("fully-booked");
                }
                // Mark weekends **only if they are after today**
                if (date > today && (date.getDay() === 0 || date.getDay() === 6)) {
                    dayElem.classList.add("weekend");
                }
            },
            onChange: function (selectedDates, dateStr, instance) {
                console.log("Selected Date:", dateStr);

                // Update both hidden and visible input fields
                $('#datepicker').val(dateStr).trigger('change');
                $('#date-input').val(dateStr);
            }
        };

        flatpickr("#calendar", flatpickrOptions);
    });

    // Handle date selection and fetch available slots
    $('#datepicker').change(function () {
        const selectedDate = $(this).val();

        $.get('/book/available-slots', { date: selectedDate }, function (data) {
            $('#time-slots').empty();

            if (data.every(slot => !slot.available)) {
                fullyBookedDates.add(selectedDate);
            } else {
                fullyBookedDates.delete(selectedDate);
            }

            data.forEach(function (slot) {
                const slotClass = slot.available ? 'available' : 'booked';
                const slotHtml = `
                    <label for="time_${slot.time}" class="slot ${slotClass}">
                        <input type="radio" id="time_${slot.time}" name="time" value="${slot.time}"
                               class="${slotClass}" ${slot.available ? '' : 'disabled'} required>
                        <span>${slot.time}</span>
                    </label>
                `;
                $('#time-slots').append(slotHtml);
            });
        }).fail(function () {
            console.error("Error fetching available slots.");
        });
    });

    // Prevent form submission if no date is selected
    $('form').submit(function (event) {
        const selectedDate = $('#datepicker').val();
        if (!selectedDate) {
            alert("Please select a date before booking.");
            event.preventDefault();
        }
    });
});
