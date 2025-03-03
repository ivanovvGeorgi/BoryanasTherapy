document.addEventListener('DOMContentLoaded', function() {
    const faqItems = document.querySelectorAll('.faq-item');

    faqItems.forEach(item => {
        const question = item.querySelector('.faq-question');
        const answer = item.querySelector('.faq-answer');
        const toggleButton = item.querySelector('.faq-toggle');

        // Initialize with the plus icon
        toggleButton.innerHTML = '<i class="fas fa-plus"></i>';

        question.addEventListener('click', () => {
            // Check if the answer is currently displayed
            if (answer.style.display === 'block') {
                // If visible, hide it
                answer.style.display = 'none';
                answer.style.opacity = '0';
                answer.style.transform = 'translateY(20px)';
                toggleButton.innerHTML = '<i class="fas fa-plus"></i>'; // Change to plus icon
            } else {
                // If not visible, show it with animation
                answer.style.display = 'block';
                setTimeout(() => {
                    answer.style.opacity = '1';
                    answer.style.transform = 'translateY(0)';
                }, 10); // Small delay to trigger the transition
                toggleButton.innerHTML = '<i class="fas fa-minus"></i>'; // Change to minus icon
            }
        });
    });
});
