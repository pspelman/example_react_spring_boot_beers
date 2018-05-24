import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';


//for typescript version
//
// class App extends React.Component<{}, any>{}
class BeerList extends React.Component {

    constructor(props: any) {
        super(props);
        //set default state
        this.state = {
            beers: [],
            isLoading: false
        }
    }

    componentDidMount() {
        this.setState({isLoading: true});

        //fetch is an api, alternative to xhr - available in most browsers
        fetch('http://localhost:8080/good-beers')
            .then(response => response.json())
            .then(data => this.setState({beers: data, isLoading: false}));

    }


    render() {
        //import the state
        const {beers, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }


        return (
            <div>
                <h2>Beer List</h2>
                {beers.map((beer) =>
                    <div key={beer.id}>
                        {beer.name}
                    </div>
                )}
            </div>
        );
    }
}

export default BeerList;
