document.addEventListener("DOMContentLoaded", function() {
    const images = document.querySelectorAll(".certificate-img"); // Select all certificate images
    const lightbox = document.createElement("div"); // Create the lightbox element
    lightbox.classList.add("lightbox");
    document.body.appendChild(lightbox); // Append the lightbox to the body

    images.forEach(image => {
        image.addEventListener("click", function() {
            lightbox.innerHTML = `<img src="${this.src}" alt="Certificate">`;  // Set the clicked image inside the lightbox
            lightbox.classList.add("show"); // Show the lightbox
        });
    });

    lightbox.addEventListener("click", function() {
        lightbox.classList.remove("show"); // Hide the lightbox when clicked outside the image
    });
});
