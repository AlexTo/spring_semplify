import React, {useState} from 'react';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import {makeStyles} from '@material-ui/styles';
import {Grid, Typography, Button} from '@material-ui/core';
import AddDocumentsDialog from './AddDocumentsDialog';

const useStyles = makeStyles(() => ({
    root: {}
}));

function Header({className, onAddDocument, ...rest}) {
    const classes = useStyles();

    return (
        <div {...rest} className={clsx(classes.root, className)}>
            <Grid
                alignItems="flex-end"
                container
                justify="space-between"
                spacing={3}>
                <Grid item>
                    <Typography
                        component="h2"
                        gutterBottom
                        variant="overline">
                        Search
                    </Typography>
                    <Typography
                        component="h1"
                        variant="h3">
                        Indexed Documents
                    </Typography>
                </Grid>
                <Grid item>
                    <Button onClick={onAddDocument}
                            color="primary"
                            variant="contained">
                        Add documents
                    </Button>
                </Grid>
            </Grid>

        </div>
    );
}

Header.propTypes = {
    className: PropTypes.string
};

export default Header;
