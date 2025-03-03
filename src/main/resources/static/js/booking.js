$(document).ready(function() {
    let fullyBookedDates = new Set();
    let unavailableDates = new Set();

    function disableWeekends(date) {
        return (date.getDay() === 0 || date.getDay() === 6);
    }

    function fetchFullyBookedDates() {
        return $.get('/public/fully-booked-dates')
            .then(function(data) {
                console.log("Fully Booked Dates:", data);
                fullyBookedDates = new Set(data);
            })
            .catch(function(error) {
                console.error("Error fetching fully booked dates:", error);
            });
    }

    function fetchUnavailableDates() {
        return $.get('/public/unavailable-dates')
            .then(function(data) {
                console.log("Unavailable Dates:", data);
                unavailableDates = new Set(data);
            })
            .catch(function(error) {
                console.error("Error fetching unavailable dates:", error);
            });
    }
    // Function to get the user's preferred language
    function getUserLanguage() {
        let lang = "bg"; // Default to Bulgarian

        // 1. Check for URL parameter (e.g., ?lang=en)
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has("lang")) {
            lang = urlParams.get("lang");
        }

        return lang;
    }

    // Function to load the Flatpickr locale
    function loadFlatpickrLocale(lang) {
        if (lang !== "en") { // Only load if not English (default)
            const script = document.createElement("script");
            script.src = `https://cdn.jsdelivr.net/npm/flatpickr@latest/dist/l10n/${lang}.js`;
            document.head.appendChild(script);
        }
    }

    // Get user language and load locale
    const userLang = getUserLanguage();
    loadFlatpickrLocale(userLang);

    // Fetch fully booked and unavailable dates before initializing Flatpickr
    Promise.all([fetchFullyBookedDates(), fetchUnavailableDates()])
        .then(() => {
            flatpickr("#datepicker", {
                firstDayOfWeek: 1,
                dateFormat: "Y-m-d", // Backend format (hidden)
                altInput: true,
                altFormat: "d-m-Y", // Display format (shown to the user)
                minDate: new Date().fp_incr(1),
                disable: [...Array.from(fullyBookedDates), ...Array.from(unavailableDates), disableWeekends],
                locale: {
                    ...flatpickr.l10ns[userLang],
                    firstDayOfWeek: 1,
                },
                onDayCreate: function(dObj, dStr, fp, dayElem) {
                    let dateStr = new Date(dayElem.dateObj.getTime() - dayElem.dateObj.getTimezoneOffset() * 60000)
                        .toISOString().split('T')[0];

                    let date = new Date(dateStr);
                    let today = new Date();
                    today.setHours(0, 0, 0, 0); // Reset time to midnight

                    // Mark fully booked dates
                    if (fullyBookedDates.has(dateStr)) {
                        dayElem.classList.add("fully-booked");
                    }

                    // Mark weekends **only if they are after today**
                    if (date > today && (date.getDay() === 0 || date.getDay() === 6)) {
                        dayElem.classList.add("weekend");
                    }
                },
                onChange: function(selectedDates, dateStr, instance) {
                    console.log("Selected Date in Flatpickr:", dateStr);
                    $('#datepicker').trigger('change');
                }
            });
        });

    $('#datepicker').change(function() {
        const selectedDate = $(this).val();
        console.log("Selected Date: ", selectedDate);

        $.get('/book/available-slots', { date: selectedDate }, function(data) {
            console.log("Available Slots: ", data);
            $('#time-slots').empty();

            if (data.every(slot => !slot.available)) {
                fullyBookedDates.add(selectedDate);
                $(`.flatpickr-day[aria-label="${selectedDate}"]`).addClass("fully-booked");
            } else {
                fullyBookedDates.delete(selectedDate);
                $(`.flatpickr-day[aria-label="${selectedDate}"]`).removeClass("fully-booked");
            }

            data.forEach(function(slot) {
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
        }).fail(function() {
            console.error("Error fetching available slots.");
        });
    });

    $('form').submit(function(event) {
        const selectedDate = $('#datepicker').val();
        if (!selectedDate) {
            alert("Please select a date before booking.");
            event.preventDefault();
        }
    });
});
