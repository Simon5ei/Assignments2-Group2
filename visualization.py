import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

def load_data(filename):
    return pd.read_csv(filename)

def plot_operations_by_data_type(df, operation):
    plt.figure(figsize=(12, 6))
    sns.barplot(x='Size', y=operation, hue='TreeType', 
                data=df, palette='Set2', ci=None)
    plt.title(f'{operation} Time by Data Size and Tree Type')
    plt.ylabel('Time (ns)')
    plt.xlabel('Data Size')
    plt.yscale('log')
    plt.legend(title='Tree Type')
    plt.tight_layout()
    plt.savefig(f'{operation.lower()}_time.png')
    plt.close()

def plot_comparison_by_data_type(df):
    data_types = df['DataType'].unique()
    operations = ['InsertTime(ns)', 'SearchTime(ns)', 'DeleteTime(ns)']
    
    for data_type in data_types:
        plt.figure(figsize=(15, 5))
        for i, operation in enumerate(operations, 1):
            plt.subplot(1, 3, i)
            subset = df[df['DataType'] == data_type]
            sns.barplot(x='Size', y=operation, hue='TreeType', 
                        data=subset, palette='Set2', ci=None)
            plt.title(f'{operation} - {data_type}')
            plt.ylabel('Time (ns)')
            plt.xlabel('Data Size')
            plt.yscale('log')
            plt.legend(title='Tree Type')
        plt.tight_layout()
        plt.savefig(f'{data_type}_comparison.png')
        plt.close()

def plot_all_operations(df):
    plt.figure(figsize=(15, 10))
    operations = ['InsertTime(ns)', 'SearchTime(ns)', 'DeleteTime(ns)']
    
    for i, operation in enumerate(operations, 1):
        plt.subplot(2, 2, i)
        sns.barplot(x='Size', y=operation, hue='TreeType', 
                    data=df, palette='Set2', ci=None)
        plt.title(operation)
        plt.ylabel('Time (ns)')
        plt.xlabel('Data Size')
        plt.yscale('log')
        plt.legend(title='Tree Type')
    
    plt.tight_layout()
    plt.savefig('all_operations.png')
    plt.close()

def main():
    # Load data
    df = load_data('performance_results.csv')
    
    # Set style
    sns.set(style='whitegrid')
    plt.style.use('seaborn-v0_8')  # Updated style
    
    # Plot individual operations
    for operation in ['InsertTime(ns)', 'SearchTime(ns)', 'DeleteTime(ns)']:
        plot_operations_by_data_type(df, operation)
    
    # Plot comparison by data type
    plot_comparison_by_data_type(df)
    
    # Plot all operations
    plot_all_operations(df)

if __name__ == "__main__":
    main() 