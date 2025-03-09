document.addEventListener('DOMContentLoaded', function() {
    const faqItems = document.querySelectorAll('.faq-item');

    faqItems.forEach(item => {
        const question = item.querySelector('.faq-question');
        const answer = item.querySelector('.faq-answer');
        const toggleButton = item.querySelector('.faq-toggle');

        // Initialize with the plus icon
        toggleButton.innerHTML = '<i class="fas fa-plus"></i>';

        // Add a smooth rotation transition to the icon
        toggleButton.querySelector('i').style.transition = 'transform 0.3s ease';

        question.addEventListener('click', () => {
            // Check if the answer is currently displayed
            if (answer.style.display === 'block') {
                // If visible, hide it with smooth reverse transition
                answer.style.opacity = '0';
                answer.style.transform = 'translateY(20px)';

                // First, change the icon to plus
                toggleButton.innerHTML = '<i class="fas fa-plus"></i>';

                // After a small delay, apply the rotation for the plus icon (reset to 0deg)
                setTimeout(() => {
                    toggleButton.querySelector('i').style.transform = 'rotate(0deg)';
                }, 50); // Small delay for smooth transition

                // Hide the answer after transition ends
                setTimeout(() => {
                    answer.style.display = 'none';
                }, 300); // Match the transition duration for opacity/transform

            } else {
                // If not visible, show it with animation
                answer.style.display = 'block';
                setTimeout(() => {
                    answer.style.opacity = '1';
                    answer.style.transform = 'translateY(0)';
                }, 10); // Small delay to trigger the transition

                // First, change the icon to minus
                toggleButton.innerHTML = '<i class="fas fa-minus"></i>';

                // After a small delay, apply the rotation for the minus icon (rotate 180deg)
                setTimeout(() => {
                    toggleButton.querySelector('i').style.transform = 'rotate(180deg)';
                }, 50); // Small delay for smooth transition
            }
        });
    });
});
