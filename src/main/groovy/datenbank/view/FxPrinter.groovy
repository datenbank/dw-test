colError.setCellFactory(new Callback<TableColumn<TestCase, Integer>, TableCell<TestCase, Integer>>() {
			@Override
			public TableCell<TestCase, Integer> call(TableColumn<TestCase, Integer> param) {
				return new TableCell<TestCase, Integer>() {

					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						if(item == -1) {
							setTextFill(Color.BLACK);
							setText("")
						}
						if(item == 1) {
							setTextFill(Color.RED);
							setText("FAILURE")
						}
						if(item == 0) { 
							setTextFill(Color.GREEN);
							setText("SUCCESS")
						}
					}
				};
			}
		});
