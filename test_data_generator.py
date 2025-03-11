import csv
import random
import numpy as np

def generate_random_data(size):
    return [random.randint(0, size * 10) for _ in range(size)]

def generate_sorted_data(size):
    return sorted(generate_random_data(size))

def generate_test_data(sizes):
    test_data = []
    for size in sizes:
        test_data.append(('random', size, generate_random_data(size)))
        test_data.append(('sorted', size, generate_sorted_data(size)))
    return test_data

def save_to_csv(test_data, filename):
    with open(filename, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['data_type', 'size', 'data'])
        for data_type, size, data in test_data:
            writer.writerow([data_type, size, ' '.join(map(str, data))])

def main():
    sizes = [1000, 10000, 100000]
    # , 1000000, 10000000]  # Different data sizes
    test_data = generate_test_data(sizes)
    save_to_csv(test_data, 'test_data.csv')
    print("Test data generated and saved to test_data.csv")

if __name__ == "__main__":
    main() 